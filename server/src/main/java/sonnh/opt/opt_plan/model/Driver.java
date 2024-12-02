package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;

/**
 * Entity class representing a Driver in the system Handles driver information,
 * location tracking, and rating management
 */
@Entity
@Table(name = "drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
		"hibernateLazyInitializer", "handler"
})
public class Driver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(nullable = false, unique = true, length = 15)
	private String phone;

	@Column(nullable = false, length = 20)
	private String licenseNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VehicleType vehicleType;

	@Column(nullable = false, length = 10)
	private String vehiclePlate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DriverStatus status;

	private Double currentLatitude;

	private Double currentLongitude;

	@Column
	private LocalDateTime lastLocationUpdate;

	private Double rating;

	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("driver")
	private List<Delivery> deliveries;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime lastUpdated;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		status = DriverStatus.AVAILABLE;
		rating = 5.0;
	}

	@PreUpdate
	protected void onUpdate() { lastUpdated = LocalDateTime.now(); }

	/**
	 * Updates driver's current location with timestamp
	 * 
	 * @param latitude  New latitude coordinate
	 * @param longitude New longitude coordinate
	 */
	public void updateLocation(Double latitude, Double longitude) {
		if (latitude == null || longitude == null) {
			throw new IllegalArgumentException("Latitude and longitude cannot be null");
		}
		this.currentLatitude = latitude;
		this.currentLongitude = longitude;
		this.lastLocationUpdate = LocalDateTime.now();
	}

	/**
	 * Updates driver's rating using weighted average
	 * 
	 * @param newRating New rating to be added (1.0 - 5.0)
	 */
	public void updateRating(Double newRating) {
		if (newRating == null || newRating < 1.0 || newRating > 5.0) {
			throw new IllegalArgumentException("Rating must be between 1.0 and 5.0");
		}

		this.rating = Objects.isNull(this.rating) ? newRating
				: (this.rating + newRating) / 2;
	}
}