package sonnh.opt.opt_plan.constant.enums;

public enum VehicleType {
	MOTORCYCLE(100.0), // Max weight 100kg
	CAR(300.0), // Max weight 300kg
	VAN(500.0), // Max weight 500kg
	TRUCK(2000.0); // Max weight 2000kg

	private final Double maxWeight;

	VehicleType(Double maxWeight) { this.maxWeight = maxWeight; }

	/**
	 * Get the maximum weight capacity for this vehicle type
	 * 
	 * @return maximum weight in kg that this vehicle can carry
	 */
	public Double getMaxWeight() { return maxWeight; }

	/**
	 * Determine required vehicle type based on order weight
	 * 
	 * @param weight Weight of the order in kg
	 * @return The most suitable vehicle type for the given weight
	 */
	public static VehicleType getRequiredVehicleType(Double weight) {
		if (weight == null || weight <= 0) {
			throw new IllegalArgumentException("Weight must be positive");
		}

		for (VehicleType type : VehicleType.values()) {
			if (weight <= type.maxWeight) {
				return type;
			}
		}
		return TRUCK; // Default to largest vehicle if weight exceeds all limits
	}
}