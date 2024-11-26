package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.StorageLocationType;
import sonnh.opt.opt_plan.model.StorageLocation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageLocationDTO {
	private Long id;
	private String code;
	private StorageAreaDTO storageArea;
	private StorageLocationType type;
	private Double length;
	private Double width;
	private Double height;
	private Double maxWeight;
	private Boolean isOccupied;
	private Integer level;
	private Integer position;

	/**
	 * Convert StorageLocation entity to StorageLocationDTO
	 * 
	 * @param storageLocation StorageLocation entity to convert
	 * @return StorageLocationDTO object with mapped fields
	 */
	public static StorageLocationDTO fromEntity(StorageLocation storageLocation) {
		if (storageLocation == null) {
			return null;
		}

		return StorageLocationDTO.builder().id(storageLocation.getId())
				.code(storageLocation.getCode())
				.storageArea(StorageAreaDTO.fromEntity(storageLocation.getStorageArea()))
				.type(storageLocation.getType()).length(storageLocation.getLength())
				.width(storageLocation.getWidth()).height(storageLocation.getHeight())
				.maxWeight(storageLocation.getMaxWeight())
				.isOccupied(storageLocation.getIsOccupied())
				.level(storageLocation.getLevel()).position(storageLocation.getPosition())
				.build();
	}
}