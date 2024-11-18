package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.WarehouseStatus;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;
import sonnh.opt.opt_plan.model.Warehouse;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Builder
public class WarehouseDTO {
	private Long id;
	private String code;
	private String name;
	private String address;
	private String phone;
	private String email;
	private Double latitude;
	private Double longitude;
	private Integer totalCapacity;
	private Integer currentOccupancy;
	private Double utilizationRate;
	private LocalTime openingTime;
	private LocalTime closingTime;
	private WarehouseType type;
	private WarehouseStatus status;
	private Boolean isActive;
	private Integer totalProducts;
	private Integer lowStockProducts;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static WarehouseDTO fromEntity(Warehouse warehouse) {
		if (warehouse == null)
			return null;
		return WarehouseDTO.builder().id(warehouse.getId()).name(warehouse.getName())
				.address(warehouse.getAddress()).code(warehouse.getCode()).build();
	}
}