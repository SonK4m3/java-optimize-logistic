package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
	@NotBlank(message = "Vehicle code is required")
	@Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Vehicle code must be 6-10 characters, uppercase letters and numbers only")
	private String vehicleCode;

	@NotNull(message = "Capacity is required")
	@Positive(message = "Capacity must be positive")
	private Double capacity;

	@NotNull(message = "Cost per km is required")
	@PositiveOrZero(message = "Cost per km must not be negative")
	private Double costPerKm;

	@NotNull(message = "Initial latitude is required")
	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	private Double initialLat;

	@NotNull(message = "Initial longitude is required")
	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	private Double initialLng;
}