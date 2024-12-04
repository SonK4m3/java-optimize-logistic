
package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;

@Data
public class WarehouseCreateRequest {
	@NotBlank(message = "Warehouse name is required")
	private String name;

	@NotBlank(message = "Address is required")
	private String address;

	@NotNull(message = "Latitude is required")
	private Double latitude;

	@NotNull(message = "Longitude is required")
	private Double longitude;

	@NotNull(message = "Total capacity is required")
	@Positive(message = "Total capacity must be positive")
	private Integer totalCapacity;

	@NotNull(message = "Total area is required")
	@Positive(message = "Total area must be positive")
	private Double totalArea;

	@NotNull(message = "Warehouse type is required")
	private WarehouseType type;

	@NotNull(message = "Manager is required")
	private Long managerId;
}