package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.StorageCondition;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

	@NotBlank(message = "Product name is required")
	private String name;

	@NotBlank(message = "Unit of measurement is required")
	private String unit;

	@Positive(message = "Price must be positive")
	private Double price;

	private StorageCondition storageCondition;
}