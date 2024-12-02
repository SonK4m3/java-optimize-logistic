package sonnh.opt.opt_plan.constant.enums;

public enum DeliveryStatus {
	PENDING, // Initial state
	ASSIGNED, // Driver assigned
	PICKED_UP, // Driver picked up from warehouse
	IN_TRANSIT, // On the way to customer
	ARRIVED, // Arrived at customer location
	DELIVERED, // Successfully delivered
	FAILED, // Delivery failed
	CANCELLED // Delivery cancelled
}