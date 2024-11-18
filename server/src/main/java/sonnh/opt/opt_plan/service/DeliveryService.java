package sonnh.opt.opt_plan.service;

import java.util.List;

import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.payload.request.DeliveryCreateRequest;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

public interface DeliveryService {
	/**
	 * Get all pending deliveries that need to be assigned to routes
	 * 
	 * @return List of pending deliveries
	 */
	List<Delivery> getPendingDeliveries();

	/**
	 * Create new delivery request
	 * 
	 * @param request Delivery creation request
	 * @return Created delivery
	 */
	Delivery createDelivery(DeliveryCreateRequest request);

	/**
	 * Update delivery status
	 * 
	 * @param deliveryId Delivery ID
	 * @param status     New status
	 * @return Updated delivery
	 */
	Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus status);
}