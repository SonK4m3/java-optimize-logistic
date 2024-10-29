package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.model.Order;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
	Order createOrder(Order order);

	Order updateOrder(Long id, Order order);

	Order getOrderById(Long id);

	Order getOrderByCode(String orderCode);

	List<Order> getAllOrders();

	List<Order> getOrdersByStatus(OrderStatus status);

	List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

	List<Order> getOrdersByUser(Long userId);

	void deleteOrder(Long id);

	Order updateOrderStatus(Long id, OrderStatus status);
}