package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import java.time.LocalTime;

@Data
public class DriverCreateRequest {
	@NotBlank(message = "Full name is required")
	private String fullName;

	@NotBlank(message = "Phone number is required")
	private String phone;

	private String email;
	private String licenseNumber;

	@NotNull(message = "Vehicle type is required")
	private VehicleType vehicleType;

	@NotBlank(message = "Vehicle plate number is required")
	private String vehiclePlateNumber;

	private Double vehicleCapacity;

	@NotNull(message = "Work start time is required")
	private LocalTime workStartTime;

	@NotNull(message = "Work end time is required")
	private LocalTime workEndTime;

	private String preferredAreas;
	private Double maxDeliveryRadius;
	private Double baseRate;
	private Double ratePerKm;
}