package sonnh.opt.opt_plan.constant.enums;

/**
 * Enum representing the status of a delivery stop.
 */
public enum DeliveryStopStatus {
	PENDING, // Stop is scheduled but not yet started
	IN_PROGRESS, // Driver is currently at or heading to the stop
	COMPLETED, // Stop has been completed
	SKIPPED, // Stop was skipped for some reason
	FAILED // Stop could not be completed
}