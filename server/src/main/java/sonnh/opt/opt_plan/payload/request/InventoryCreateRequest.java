package sonnh.opt.opt_plan.payload.request;

import lombok.Data;

@Data
public class InventoryCreateRequest {
	private Long warehouseId;
	private Long productId;
	private Integer quantity;
}
