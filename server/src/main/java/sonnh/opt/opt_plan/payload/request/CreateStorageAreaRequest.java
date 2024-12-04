package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.StorageAreaType;

@Data
public class CreateStorageAreaRequest {
	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Type is required")
	private StorageAreaType type;

	@NotNull(message = "Warehouse ID is required")
	private Long warehouseId;

	@Positive(message = "Area must be positive")
	private Integer area;
}