package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.InventoryUpdateType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdateRequest {
	@NotNull(message = "Product ID is required")
	private Long productId;

	@NotNull(message = "Quantity is required")
	private Integer quantity;

	@NotNull(message = "Update type is required")
	private InventoryUpdateType updateType; // ADD, SUBTRACT, SET
}