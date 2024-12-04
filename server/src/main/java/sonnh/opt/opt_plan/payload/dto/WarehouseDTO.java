package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.WarehouseStatus;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;
import sonnh.opt.opt_plan.model.Warehouse;

import java.time.LocalDateTime;

@Data
@Builder
public class WarehouseDTO {
	private Long id;
	private String code;
	private String name;
	private LocationDTO location;
	private WarehouseStatus status;
	private WarehouseType type;
	private Double totalArea;
	private Integer totalCapacity;
	private StaffDTO manager;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static WarehouseDTO fromEntity(Warehouse warehouse) {
		if (warehouse == null)
			return null;
		return WarehouseDTO.builder().id(warehouse.getId()).name(warehouse.getName())
				.location(LocationDTO.fromEntity(warehouse.getLocation()))
				.status(warehouse.getStatus()).type(warehouse.getType())
				.totalArea(warehouse.getTotalArea())
				.totalCapacity(warehouse.getTotalCapacity())
				.manager(StaffDTO.fromEntity(warehouse.getManager()))
				.createdAt(warehouse.getCreatedAt()).updatedAt(warehouse.getUpdatedAt())
				.build();
	}
}