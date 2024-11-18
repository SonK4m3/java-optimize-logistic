package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VRPResultDTO {
	private List<VehicleDTO> vehicles;
	private Double totalDistance;
	private Double totalTime;
	private Integer totalOrders;
	private Double totalCost;
	private String summary;
}