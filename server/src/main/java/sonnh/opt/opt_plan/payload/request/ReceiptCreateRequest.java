package sonnh.opt.opt_plan.payload.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.ReceiptType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptCreateRequest {
	@NotNull(message = "Items cannot be null")
	@Size(min = 1, message = "At least one item is required")
	private List<ReceiptItemRequest> items;

	private Long storageLocationId;

	private String notes;

	private ReceiptType type;
}