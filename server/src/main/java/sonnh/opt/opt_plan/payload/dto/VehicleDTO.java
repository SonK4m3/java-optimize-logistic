package sonnh.opt.opt_plan.payload.dto;

import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.constant.enums.RouteStatus;
import sonnh.opt.opt_plan.constant.enums.StopStatus;
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
				.currentUtilization(calculateUtilization(vehicle))
				.activeDeliveries(countActiveDeliveries(vehicle)).build();
	}

	/**
	 * Convert DTO to Entity
	 * 
	 * @return Vehicle entity
	 */
	public Vehicle toEntity() {
		return Vehicle.builder().id(this.id).vehicleCode(this.vehicleCode)
				.capacity(this.capacity).costPerKm(this.costPerKm).status(this.status)
				.currentLat(this.currentLat).currentLng(this.currentLng).build();
	}

	private static Double calculateUtilization(Vehicle vehicle) {
		if (vehicle.getRoutes() == null || vehicle.getRoutes().isEmpty()) {
			return 0.0;
		}

		Double currentLoad = vehicle.getRoutes().stream()
				.filter(route -> route.getStatus() == RouteStatus.IN_PROGRESS)
				.flatMap(route -> route.getStops().stream())
				.filter(stop -> stop.getStatus() == StopStatus.PENDING)
				.mapToDouble(stop -> stop.getDelivery().getOrder().getWeight()).sum();

		return (currentLoad / vehicle.getCapacity()) * 100;
	}

	private static Integer countActiveDeliveries(Vehicle vehicle) {
		if (vehicle.getRoutes() == null)
			return 0;

		return (int) vehicle.getRoutes().stream()
				.filter(route -> route.getStatus() == RouteStatus.IN_PROGRESS)
				.flatMap(route -> route.getStops().stream())
				.filter(stop -> stop.getStatus() == StopStatus.PENDING).count();
	}
}
