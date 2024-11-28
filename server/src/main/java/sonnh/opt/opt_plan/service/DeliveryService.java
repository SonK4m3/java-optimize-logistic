package sonnh.opt.opt_plan.service;

import java.util.List;

import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.payload.request.DeliveryUpdateRequest;

public interface DeliveryService {
	List<Delivery> getDeliveriesByOrderId(Long orderId);

	Delivery updateDeliveryStatus(Long deliveryId, DeliveryUpdateRequest request);

	Delivery assignDriver(Long deliveryId, Long driverId);
}