package sonnh.opt.opt_plan.service;

import org.springframework.data.domain.Page;

import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.payload.request.OrderCreateRequest;
import sonnh.opt.opt_plan.payload.request.PageParams;

import java.util.List;

public interface OrderService {
	Order createOrder(OrderCreateRequest orderRequest);

	List<Order> getAllOrders();

	Page<Order> getOrdersByUser(PageParams pageParams);
}