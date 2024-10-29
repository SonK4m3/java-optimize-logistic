package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.repository.OrderRepository;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	@Override
	public Order createOrder(Order order) {
		// Additional business logic can be added here
		return orderRepository.save(order);
	}

	@Override
	public Order updateOrder(Long id, Order order) {
		Order existingOrder = getOrderById(id);
		existingOrder.setStatus(order.getStatus());
		existingOrder.setTotalAmount(order.getTotalAmount());
		return orderRepository.save(existingOrder);
	}

	@Override
	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
	}

	@Override
	public Order getOrderByCode(String orderCode) {
		return orderRepository.findByOrderCode(orderCode)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "orderCode", orderCode));
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public List<Order> getOrdersByStatus(OrderStatus status) {
		return orderRepository.findByStatus(status);
	}

	@Override
	public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		return orderRepository.findByOrderDateBetween(startDate, endDate);
	}

	@Override
	public List<Order> getOrdersByUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		return orderRepository.findByUser(user);
	}

	@Override
	public void deleteOrder(Long id) {
		Order order = getOrderById(id);
		orderRepository.delete(order);
	}

	@Override
	public Order updateOrderStatus(Long id, OrderStatus status) {
		Order order = getOrderById(id);
		order.setStatus(status);
		return orderRepository.save(order);
	}
}