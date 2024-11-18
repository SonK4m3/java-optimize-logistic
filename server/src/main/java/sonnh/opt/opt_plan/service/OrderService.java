package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.DeliveryDTO;
import sonnh.opt.opt_plan.payload.dto.OrderDTO;
import sonnh.opt.opt_plan.payload.request.OrderCreateRequest;
import sonnh.opt.opt_plan.payload.request.PageParams;
import sonnh.opt.opt_plan.payload.response.PageResponse;

import java.util.List;

public interface OrderService {
	OrderDTO createOrder(OrderCreateRequest orderRequest);

	List<OrderDTO> getAllOrders();

	List<OrderDTO> getOrdersByWarehouse(Long warehouseId);

	DeliveryDTO acceptOrderForDelivery(Long driverId, Long orderId);

	DeliveryDTO denyOrderForDelivery(Long driverId, Long orderId);

	List<DeliveryDTO> inTransitDelivery(Long driverId);

	DeliveryDTO completeDelivery(Long driverId, Long deliveryId);

	DeliveryDTO cancelDelivery(Long driverId, Long deliveryId);

	PageResponse<OrderDTO> getOrdersByUser(PageParams pageParams);

	List<OrderDTO> getOrdersBySenderId(Long senderId);
}