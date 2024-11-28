package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.exception.AuthenticationException;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.exception.InsufficientStockException;
import sonnh.opt.opt_plan.model.*;
import sonnh.opt.opt_plan.payload.request.OrderCreateRequest;
import sonnh.opt.opt_plan.payload.request.OrderCreateRequest.OrderItemRequest;
import sonnh.opt.opt_plan.repository.*;
import sonnh.opt.opt_plan.service.OrderService;
import sonnh.opt.opt_plan.utils.SecurityUtils;
import sonnh.opt.opt_plan.payload.request.PageParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sonnh.opt.opt_plan.service.InventoryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
	private final SecurityUtils securityUtils;
	private final OrderRepository orderRepository;
	private final DeliveryRepository deliveryRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final LocationRepository locationRepository;
	private final InventoryService inventoryService;

	@Override
	@Transactional
	public Order createOrder(OrderCreateRequest orderRequest) {
		// Input: OrderCreateRequest containing customer, location, items
		// details
		// Output: Created Order with delivery and order details

		// Validate and get customer
		Customer customer = customerRepository.findById(orderRequest.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException(String.format(
						"Customer not found with id: %d", orderRequest.getCustomerId())));

		// Validate and get pickup location
		Location pickupLocation = locationRepository
				.findById(orderRequest.getPickupLocationId())
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format("Pickup location not found with id: %d",
								orderRequest.getPickupLocationId())));

		// Create initial order
		Order order = createInitialOrder(customer, orderRequest.getPriority());

		// Create order details
		List<OrderDetail> orderDetails = createOrderDetails(orderRequest.getItems(),
				order);

		// Calculate order totals
		double totalAmount = calculateTotalAmount(orderDetails);
		double totalWeight = calculateTotalWeight(orderDetails);

		// Create delivery
		createDelivery(orderRequest.getDeliveryNote(), pickupLocation, order);

		// Update and save final order
		return updateAndSaveOrder(order, orderDetails, totalAmount, totalWeight);
	}

	private Order createInitialOrder(Customer customer, OrderPriority priority) {
		Order order = Order.builder().customer(customer).status(OrderStatus.PENDING)
				.priority(priority).totalAmount(0.0).totalWeight(0.0).build();
		return orderRepository.save(order);
	}

	private List<OrderDetail> createOrderDetails(List<OrderItemRequest> items,
			Order order) {
		List<OrderDetail> orderDetails = new ArrayList<>();

		for (OrderItemRequest orderItem : items) {
			if (!inventoryService.checkStockAvailability(orderItem.getProductId(),
					orderItem.getQuantity())) {
				throw new InsufficientStockException(
						String.format("Insufficient stock for product with id: %d",
								orderItem.getProductId()));
			}

			Product product = productRepository.findById(orderItem.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException(String.format(
							"Product not found with id: %d", orderItem.getProductId())));

			OrderDetail orderDetail = OrderDetail.builder().product(product)
					.quantity(orderItem.getQuantity())
					.price(product.getPrice() * orderItem.getQuantity())
					.weight(product.getWeight() * orderItem.getQuantity()).order(order)
					.build();

			orderDetails.add(orderDetailRepository.save(orderDetail));
		}

		return orderDetails;
	}

	private double calculateTotalAmount(List<OrderDetail> orderDetails) {
		return orderDetails.stream().mapToDouble(OrderDetail::getPrice).sum();
	}

	private double calculateTotalWeight(List<OrderDetail> orderDetails) {
		return orderDetails.stream().mapToDouble(OrderDetail::getWeight).sum();
	}

	private Delivery createDelivery(String deliveryNote, Location pickupLocation,
			Order order) {
		Delivery delivery = Delivery.builder().status(DeliveryStatus.PENDING)
				.estimatedDistance(0.0).estimatedDeliveryTime(LocalDateTime.now())
				.deliveryNote(deliveryNote).pickupLocation(pickupLocation).order(order)
				.build();
		return deliveryRepository.save(delivery);
	}

	private Order updateAndSaveOrder(Order order, List<OrderDetail> orderDetails,
			double totalAmount, double totalWeight) {
		order.setOrderDetails(orderDetails);
		order.setTotalAmount(totalAmount);
		order.setTotalWeight(totalWeight);
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() { return orderRepository.findAll(); }

	@Override
	@Transactional(readOnly = true)
	public Page<Order> getOrdersByUser(PageParams pageParams) {
		Optional<User> user = securityUtils.getCurrentUser();
		if (user.isEmpty()) {
			throw new AuthenticationException("User not authenticated");
		}

		Sort sort = Sort
				.by(pageParams.getSortDir().equalsIgnoreCase("desc") ? Sort.Direction.DESC
						: Sort.Direction.ASC, pageParams.getSortBy());

		Pageable pageable = PageRequest.of(pageParams.getPage() - 1,
				pageParams.getLimit(), sort);

		return orderRepository.findOrdersByCustomerId(user.get().getId(), pageable);
	}
}