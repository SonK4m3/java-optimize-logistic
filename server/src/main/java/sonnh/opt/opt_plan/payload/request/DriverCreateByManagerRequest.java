package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.VehicleType;

@Data
public class DriverCreateByManagerRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 100)
	private String fullName;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	@NotBlank(message = "Phone number is required")
	private String phone;

	@NotBlank(message = "License number is required")
	private String licenseNumber;

	@NotNull(message = "Vehicle type is required")
	private VehicleType vehicleType;

	@NotBlank(message = "Vehicle plate number is required")
	private String vehiclePlateNumber;
}
