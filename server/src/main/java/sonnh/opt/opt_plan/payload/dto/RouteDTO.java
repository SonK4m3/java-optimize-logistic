package sonnh.opt.opt_plan.payload.dto;

import sonnh.opt.opt_plan.model.Route;
import sonnh.opt.opt_plan.constant.enums.RouteStatus;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteDTO {
	private Long id;
	private Long vehicleId;
	private String vehicleName;
	private List<DeliveryStopDTO> stops;
	private Double totalDistance;
	private Double totalCost;
	private RouteStatus status;

	/**
	 * Convert Route entity to RouteDTO
	 * 
	 * @param route Route entity to convert
	 * @return RouteDTO object with mapped fields
	 */
	public static RouteDTO fromEntity(Route route) {
		if (route == null) {
			return null;
		}

		return RouteDTO.builder().id(route.getId())
				.vehicleId(route.getVehicle() != null ? route.getVehicle().getId() : null)
				.vehicleName(
						route.getVehicle() != null ? route.getVehicle().getVehicleCode()
								: null)
				.totalDistance(route.getTotalDistance()).totalCost(route.getTotalCost())
				.status(route.getStatus()).build();
	}
}
