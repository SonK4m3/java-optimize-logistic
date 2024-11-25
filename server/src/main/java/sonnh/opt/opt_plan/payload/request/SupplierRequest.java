package sonnh.opt.opt_plan.payload.request;

import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.SupplierStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class SupplierRequest {
	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
	private String name;

	@Nullable()
	private SupplierStatus status;
}