package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
	@NotBlank(message = "Product name is required")
	private String name;

	private String description;
	private String sku;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be positive")
	private Double price;

	@Positive(message = "Weight must be positive")
	private Double weight;

	private String dimensions;
	private Boolean isFragile;
	private Boolean isDangerous;
	private Boolean requiresRefrigeration;

	@NotNull(message = "Minimum stock level is required")
	@Positive(message = "Minimum stock level must be positive")
	private Integer minStockLevel;

	@NotNull(message = "Maximum stock level is required")
	@Positive(message = "Maximum stock level must be positive")
	private Integer maxStockLevel;

	private Long categoryId;
	private String manufacturer;
	private String origin;
	private String unit;
	private String shelfLife;
	private String storageInstructions;
	private String createdBy;
	private String updatedBy;
	private Boolean isActive;
}