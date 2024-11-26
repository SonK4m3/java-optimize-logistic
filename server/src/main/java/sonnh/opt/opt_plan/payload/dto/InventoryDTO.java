package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.constant.enums.InventoryStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
	private Long id;
	private StorageLocationDTO storageLocation;
	private ProductDTO product;
	private Integer quantity;
	private Integer minQuantity;
	private Integer maxQuantity;
	private String location;
	private InventoryStatus status;
	private LocalDateTime expiryDate;

	public static InventoryDTO fromEntity(Inventory inventory) {
		return InventoryDTO.builder().id(inventory.getId())
				.product(ProductDTO.fromEntity(inventory.getProduct()))
				.storageLocation(
						StorageLocationDTO.fromEntity(inventory.getStorageLocation()))
				.quantity(inventory.getQuantity()).minQuantity(inventory.getMinQuantity())
				.maxQuantity(inventory.getMaxQuantity()).location(inventory.getLocation())
				.status(inventory.getStatus()).expiryDate(inventory.getExpiryDate())
				.build();
	}
}