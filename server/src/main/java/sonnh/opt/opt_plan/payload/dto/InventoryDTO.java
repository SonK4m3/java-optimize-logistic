package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.model.Inventory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
	private Long id;
	private Long productId;
	private String productCode;
	private String productName;
	private Long warehouseId;
	private Integer quantity;
	private String unit;
	private LocalDateTime lastUpdated;

	public static InventoryDTO fromEntity(Inventory inventory) {
		return InventoryDTO.builder().id(inventory.getId())
				.productId(inventory.getProduct().getId())
				.productCode(inventory.getProduct().getCode())
				.productName(inventory.getProduct().getName())
				.warehouseId(inventory.getWarehouse().getId())
				.quantity(inventory.getQuantity()).unit(inventory.getProduct().getUnit())
				.lastUpdated(inventory.getLastUpdated()).build();
	}
}