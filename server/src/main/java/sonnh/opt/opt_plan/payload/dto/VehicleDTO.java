package sonnh.opt.opt_plan.payload.dto;

import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.constant.enums.RouteStatus;
import sonnh.opt.opt_plan.constant.enums.DeliveryStopStatus;
import sonnh.opt.opt_plan.model.Vehicle;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
	private Long id;
	private String vehicleCode;
	private Double capacity;
	private Double costPerKm;
	private VehicleStatus status;

	// Current location
	private Double currentLat;
	private Double currentLng;

	// Metrics
	private Double currentUtilization; // % of capacity used
	private Integer activeDeliveries; // Number of pending deliveries

	// Timestamps
	private LocalDateTime lastUpdated;
	private String lastUpdatedBy;

	/**
	 * Convert Entity to DTO
	 * 
	 * @param vehicle Vehicle entity
	 * @return VehicleDTO
	 */
	public static VehicleDTO fromEntity(Vehicle vehicle) {
		if (vehicle == null)
			return null;

		return VehicleDTO.builder().id(vehicle.getId())
				.vehicleCode(vehicle.getVehicleCode()).capacity(vehicle.getCapacity())
				.costPerKm(vehicle.getCostPerKm()).status(vehicle.getStatus())
				.currentLat(vehicle.getCurrentLat()).currentLng(vehicle.getCurrentLng())
				.build();
	}
}
