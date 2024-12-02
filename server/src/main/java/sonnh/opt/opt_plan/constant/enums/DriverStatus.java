package sonnh.opt.opt_plan.constant.enums;

/**
 * Enum representing the various states a driver can be in within the delivery
 * system. Used to track driver availability and manage delivery assignments.
 */
public enum DriverStatus {
	/**
	 * Driver is on duty and ready to accept new delivery assignments
	 */
	AVAILABLE,

	/**
	 * Driver is currently handling a delivery and cannot accept new assignments
	 */
	BUSY,

	/**
	 * Driver is not currently working or accepting any deliveries
	 */
	OFF_DUTY,

	/**
	 * Driver is on a scheduled break period and temporarily unavailable
	 */
	ON_BREAK
}