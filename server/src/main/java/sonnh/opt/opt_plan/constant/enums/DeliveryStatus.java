package sonnh.opt.opt_plan.constant.enums;

/**
 * Enum for defining the status of a delivery within the system. It includes the
 * following states: PENDING: Initial state of the delivery.
 * WAITING_FOR_DRIVER_ACCEPTANCE: Waiting for the driver to accept the delivery.
 * DRIVER_ACCEPTED: The driver has accepted the delivery. IN_TRANSIT: The
 * delivery is on its way to the customer. DELIVERED: The delivery has been
 * successfully completed. CANCELLED: The delivery has been cancelled.
 */
public enum DeliveryStatus {
	PENDING, WAITING_FOR_DRIVER_ACCEPTANCE, DRIVER_ACCEPTED, IN_TRANSIT, DELIVERED,
	CANCELLED
}