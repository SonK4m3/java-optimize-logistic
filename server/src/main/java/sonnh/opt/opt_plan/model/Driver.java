package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.model.embedded.DriverMetrics;
import sonnh.opt.opt_plan.model.embedded.WorkingSchedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

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

	@Column(nullable = false)
	private String fullName;

	private String phone;
	private String email;
	private String licenseNumber;
	private LocalDateTime licenseExpiryDate;

	// Current status and location
	@Enumerated(EnumType.STRING)
	private DriverStatus status;

	private Double currentLatitude;
	private Double currentLongitude;
	private LocalDateTime lastLocationUpdate;

	// Vehicle information
	@Enumerated(EnumType.STRING)
	private VehicleType vehicleType;

	private String vehiclePlateNumber;
	private Double vehicleCapacity; // in kg
	private String vehicleDescription;

	// Working hours and schedule
	private LocalTime workStartTime;
	private LocalTime workEndTime;
	private Integer maxWorkingHours;
	private Integer remainingWorkingMinutes;
	private LocalDateTime lastBreakTime;

	// Preferred working areas
	private String preferredAreas; // Comma-separated area codes
	private Double maxDeliveryRadius; // in km

	// Performance metrics
	private Integer totalDeliveries;
	private Integer completedDeliveries;
	private Integer cancelledDeliveries;
	private Double averageRating;
	private Integer totalReviews;

	// Cost and payment
	private Double baseRate; // Base rate per delivery
	private Double ratePerKm;
	private Double totalEarnings;

	@OneToMany(mappedBy = "driver")
	private List<Delivery> deliveries;

	// Tracking
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
	private Boolean isActive;

	@Embedded
	private WorkingSchedule schedule;

	@Embedded
	private DriverMetrics metrics;

	// Cost optimization fields
	private Double fuelCostPerKm;
	private Double maintenanceCostPerKm;
	private Double insuranceCostPerDay;

	// Optimization preferences
	private Boolean preferHighValueOrders;
	private Boolean preferShortDistances;
	private Double maxAcceptableDistance;
	private Integer minOrderValue;

	// Zone preferences
	@ElementCollection
	@CollectionTable(name = "driver_preferred_zones")
	private Set<String> preferredZones;

	// Break time management
	private LocalDateTime lastBreakStart;
	private LocalDateTime lastBreakEnd;
	private Integer totalBreakMinutesToday;

	// New methods for optimization
	public boolean isAvailableForOrder(Order order) {
		return status == DriverStatus.AVAILABLE && isWithinWorkingHours()
				&& hasCapacityFor(order) && isInPreferredZone(order)
				&& meetsValuePreference(order);
	}

	public boolean needsBreak() {
		if (lastBreakEnd == null)
			return false;
		long minutesSinceBreak = ChronoUnit.MINUTES.between(lastBreakEnd,
				LocalDateTime.now());
		return minutesSinceBreak >= schedule.getMinBreakMinutes();
	}

	private boolean isWithinWorkingHours() {
		LocalTime now = LocalTime.now();
		return !now.isBefore(schedule.getPreferredStartTime())
				&& !now.isAfter(schedule.getPreferredEndTime());
	}

	private boolean hasCapacityFor(Order order) {
		return order.getWeight() <= vehicleCapacity;
	}

	private boolean isInPreferredZone(Order order) {
		return preferredZones.contains(order.getReceiverAddress());
	}

	private boolean meetsValuePreference(Order order) {
		return order.getTotalPrice() >= minOrderValue;
	}
}