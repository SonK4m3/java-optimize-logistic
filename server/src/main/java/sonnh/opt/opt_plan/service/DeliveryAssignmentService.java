package sonnh.opt.opt_plan.service;

import java.util.List;

import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.payload.request.DeliveryAssignmentCreateRequest;

public interface DeliveryAssignmentService {
	DeliveryAssignment assignDeliveryToDriver(Long deliveryId, Long driverId);

	DeliveryAssignment acceptDelivery(Long deliveryId, Long driverId);

	DeliveryAssignment rejectDelivery(Long deliveryId, Long driverId, String reason);

	DeliveryAssignment createDeliveryAssignment(DeliveryAssignmentCreateRequest request);

	List<DeliveryAssignment> getByDriver(Long driverId);

	List<DeliveryAssignment> getByDelivery(Long deliveryId);

	DeliveryAssignment updateStatus(Long deliveryId, Long driverId,
			DeliveryStatus newStatus);

	DeliveryAssignment driverStartDelivery(Long deliveryAssignmentId);

	DeliveryAssignment driverDeliverDelivery(Long deliveryAssignmentId);

	DeliveryAssignment refreshAssignment(Long driverId);
}