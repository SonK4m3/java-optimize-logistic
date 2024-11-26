package sonnh.opt.opt_plan.payload.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InventoryCreateRequest {
	private Long warehouseId;
	private Long productId;
	private Long storageLocationId;
	private Integer quantity;
	private Integer minQuantity;
	private Integer maxQuantity;
	private LocalDateTime expiryDate;
}
