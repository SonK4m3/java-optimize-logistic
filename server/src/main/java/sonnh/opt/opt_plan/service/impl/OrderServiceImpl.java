package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.exception.AuthenticationException;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.*;
import sonnh.opt.opt_plan.payload.request.OrderCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.repository.*;
import sonnh.opt.opt_plan.service.OrderService;
import sonnh.opt.opt_plan.utils.SecurityUtils;
import sonnh.opt.opt_plan.payload.dto.DeliveryDTO;
import sonnh.opt.opt_plan.payload.dto.OrderDTO;
import sonnh.opt.opt_plan.payload.request.PageParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
	private final SecurityUtils securityUtils;
	private final OrderRepository orderRepository;
	private final WarehouseRepository warehouseRepository;
	private final DriverRepository driverRepository;
	private final DeliveryRepository deliveryRepository;
	private final OrderProductRepository orderProductRepository;
	private final LocationRepository locationRepository;

	@Override
	@Transactional
	public OrderDTO createOrder(OrderCreateRequest orderRequest) {
		Optional<User> user = securityUtils.getCurrentUser();
		if (user.isEmpty() || orderRequest.getSenderId() == null) {
			throw new AuthenticationException("User not authenticated");
		}

		if (orderRequest.getSenderId() != user.get().getId()) {
			throw new AuthenticationException("User not authenticated");
		}

		Warehouse pickupWarehouse = warehouseRepository
				.findById(orderRequest.getPickupWarehouseId())
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

		Location receiverLocation = Location.builder()
				.address(orderRequest.getReceiverAddress())
				.latitude(orderRequest.getReceiverLatitude())
				.longitude(orderRequest.getReceiverLongitude()).build();

		receiverLocation = locationRepository.save(receiverLocation);

		Order order = Order.builder().orderCode(Order.generateOrderCode())
				.sender(user.get()).status(OrderStatus.PENDING)
				.priority(OrderPriority.LOW).receiverName(orderRequest.getReceiverName())
				.receiverPhone(orderRequest.getReceiverPhone())
				.receiverLocation(receiverLocation).pickupWarehouse(pickupWarehouse)
				.pickupTime(orderRequest.getPickupTime())
				.serviceType(orderRequest.getServiceType())
				.cargoType(orderRequest.getCargoType()).payer(orderRequest.getPayer())
				.lastUpdated(LocalDateTime.now()).lastUpdatedBy(user.get().getUsername())
				.weight(orderRequest.getWeight()).totalPrice(orderRequest.getTotalPrice())
				.build();

		order = orderRepository.save(order);

		final Order finalOrder = order;
		orderRequest.getOrderProducts().forEach(orderProduct -> {
			OrderProduct op = OrderProduct.builder().name(orderProduct.getName())
					.quantity(orderProduct.getQuantity())
					.unitPrice(orderProduct.getPrice()).weight(orderProduct.getWeight())
					.order(finalOrder).build();
			op = orderProductRepository.save(op);
			finalOrder.addOrderProduct(op);
		});

		Delivery delivery = Delivery.builder().status(DeliveryStatus.PENDING)
				.deliveryNote(orderRequest.getDeliveryNote()).order(order).build();
		delivery = deliveryRepository.save(delivery);

		return OrderDTO.fromEntity(orderRepository.save(order));
	}

	@Override
	public List<OrderDTO> getAllOrders() {
		return orderRepository.findAll().stream().map(OrderDTO::fromEntity).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<OrderDTO> getOrdersByUser(PageParams pageParams) {
		Optional<User> user = securityUtils.getCurrentUser();
		if (user.isEmpty()) {
			throw new AuthenticationException("User not authenticated");
		}

		Sort sort = Sort
				.by(pageParams.getSortDir().equalsIgnoreCase("desc") ? Sort.Direction.DESC
						: Sort.Direction.ASC, pageParams.getSortBy());

		Pageable pageable = PageRequest.of(pageParams.getPage() - 1,
				pageParams.getLimit(), sort);

		Page<Order> orderPage = orderRepository.findOrdersBySenderId(user.get().getId(),
				pageable);

		List<OrderDTO> orderDTOs = orderPage.getContent().stream()
				.map(OrderDTO::fromEntity).collect(Collectors.toList());

		return PageResponse.<OrderDTO> builder().docs(orderDTOs)
				.totalDocs(orderPage.getTotalElements()).page(pageParams.getPage())
				.limit(pageParams.getLimit()).totalPages(orderPage.getTotalPages())
				.hasNextPage(orderPage.hasNext()).hasPrevPage(orderPage.hasPrevious())
				.build();
	}

	@Override
	public List<OrderDTO> getOrdersByWarehouse(Long warehouseId) {
		Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Not exist order with warehouse id: " + warehouseId));
		return orderRepository.findByPickupWarehouse(warehouse).stream()
				.map(OrderDTO::fromEntity).toList();
	}

	@Override
	public DeliveryDTO acceptOrderForDelivery(Long driverId, Long orderId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
		Delivery delivery = deliveryRepository.findByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		if (delivery.getStatus() != DeliveryStatus.PENDING) {
			throw new ResourceNotFoundException("Order is not available for delivery");
		}

		delivery.setStatus(DeliveryStatus.IN_TRANSIT);
		delivery.setDriver(driver);
		delivery.getOrder().setStatus(OrderStatus.IN_TRANSIT);

		driver.setStatus(DriverStatus.BUSY);

		driverRepository.save(driver);
		deliveryRepository.save(delivery);

		return DeliveryDTO.fromEntity(delivery);
	}

	@Override
	public DeliveryDTO denyOrderForDelivery(Long driverId, Long orderId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
		Delivery delivery = deliveryRepository.findByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		if (delivery.getStatus() != DeliveryStatus.PENDING) {
			throw new ResourceNotFoundException("Order is not available for delivery");
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		order.setStatus(OrderStatus.PENDING);
		order = orderRepository.save(order);

		delivery.setStatus(DeliveryStatus.PENDING);
		delivery.setDriver(null);
		delivery.setOrder(order);

		driver.setStatus(DriverStatus.AVAILABLE);

		driverRepository.save(driver);
		deliveryRepository.save(delivery);

		return DeliveryDTO.fromEntity(delivery);
	}

	@Override
	@Transactional
	public List<DeliveryDTO> inTransitDelivery(Long driverId) {
		Optional<Delivery> delivery = deliveryRepository.findByDriverId(driverId);
		if (delivery.isEmpty()) {
			throw new ResourceNotFoundException("Delivery not found");
		}
		// Initialize lazy collections
		Hibernate.initialize(delivery.get().getOrder().getOrderProducts());

		return List.of(DeliveryDTO.fromEntity(delivery.get()));
	}

	@Override
	public DeliveryDTO completeDelivery(Long driverId, Long deliveryId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		delivery.setStatus(DeliveryStatus.DELIVERED);
		delivery.setDeliveryDate(LocalDateTime.now());
		delivery.getOrder().setStatus(OrderStatus.DELIVERED);

		driver.setStatus(DriverStatus.AVAILABLE);

		driverRepository.save(driver);
		deliveryRepository.save(delivery);

		return DeliveryDTO.fromEntity(delivery);
	}

	@Override
	public DeliveryDTO cancelDelivery(Long driverId, Long deliveryId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		delivery.setStatus(DeliveryStatus.PENDING);
		delivery.setDriver(null);
		delivery.getOrder().setStatus(OrderStatus.PENDING);

		driver.setStatus(DriverStatus.AVAILABLE);

		driverRepository.save(driver);
		deliveryRepository.save(delivery);

		return DeliveryDTO.fromEntity(delivery);
	}

	@Override
	public List<OrderDTO> getOrdersBySenderId(Long senderId) {
		return orderRepository.findOrdersBySenderId(senderId).stream()
				.map(OrderDTO::fromEntity).toList();
	}
}