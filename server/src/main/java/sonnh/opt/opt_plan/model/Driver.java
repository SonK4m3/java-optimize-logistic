package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String driverCode;

	@NotBlank(message = "Full name is required")
	@Column(nullable = false)
	private String fullName;

	@NotBlank(message = "Phone number is required")
	@Column(nullable = false)
	private String phone;

	private String email;
	private String licenseNumber;
	private LocalDateTime licenseExpiryDate;

	@Enumerated(EnumType.STRING)
	private VehicleType type;

	private String vehiclePlateNumber;
	private Double vehicleCapacity;

	private LocalTime workStartTime;
	private LocalTime workEndTime;
	private String preferredAreas;
	private Double maxDeliveryRadius;
	private Double baseRate;
	private Double ratePerKm;

	// Current status and location
	@Enumerated(EnumType.STRING)
	private DriverStatus status;

	private Double currentLatitude;
	private Double currentLongitude;
	private LocalDateTime lastLocationUpdate;
	private boolean isActive;
	private Integer maxWorkingHours;
	private Integer remainingWorkingMinutes;
}