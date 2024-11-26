package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.StorageAreaType;
import sonnh.opt.opt_plan.model.StorageArea;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageAreaDTO {
	private Long id;
	private String name;
	private StorageAreaType type;
	private Integer area;
	private Integer currentOccupancy;
	private Boolean isActive;
	private WarehouseDTO warehouse;
	private double utilizationRate;

	public static StorageAreaDTO fromEntity(StorageArea storageArea) {
		return StorageAreaDTO.builder().id(storageArea.getId())
				.name(storageArea.getName()).type(storageArea.getType())
				.area(storageArea.getArea())
				.currentOccupancy(storageArea.getCurrentOccupancy())
				.isActive(storageArea.getIsActive())
				.warehouse(WarehouseDTO.fromEntity(storageArea.getWarehouse())).build();
	}

	public static StorageAreaDTO fromEntity(StorageArea storageArea,
			double utilizationRate) {
		return StorageAreaDTO.builder().id(storageArea.getId())
				.name(storageArea.getName()).type(storageArea.getType())
				.area(storageArea.getArea())
				.currentOccupancy(storageArea.getCurrentOccupancy())
				.isActive(storageArea.getIsActive())
				.warehouse(WarehouseDTO.fromEntity(storageArea.getWarehouse()))
				.utilizationRate(utilizationRate).build();
	}
}