package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverCreateRequest {
	private Long userId;

	@NotBlank(message = "Phone number is required")
	private String phone;

	@NotBlank(message = "License number is required")
	private String licenseNumber;

	@NotBlank(message = "Vehicle type is required")
	private String vehicleType;

	@NotBlank(message = "Vehicle plate number is required")
	private String vehiclePlateNumber;
}