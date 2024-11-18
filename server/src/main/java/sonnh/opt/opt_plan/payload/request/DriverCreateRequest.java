package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverCreateRequest {
	@NotBlank(message = "Full name is required")
	private String fullName;

	@NotBlank(message = "Phone number is required")
	private String phone;

	private String email;
	private String licenseNumber;
	private String vehicleType;
	private String vehiclePlateNumber;
	private Double vehicleCapacity;
	private String workStartTime;
	private String workEndTime;
	private String preferredAreas;
	private Double maxDeliveryRadius;
	private Double baseRate;
	private Double ratePerKm;
}