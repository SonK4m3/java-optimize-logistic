package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseSpaceDTO {
	private Long warehouseId;
	private String warehouseCode;
	private Double totalArea;
	private Double usedArea;
	private Double availableArea;
	private Integer totalCapacity;
	private Integer usedCapacity;
	private Integer availableCapacity;
	private Double spaceUtilizationPercentage;
	private Double capacityUtilizationPercentage;
}