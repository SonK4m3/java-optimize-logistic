package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.constant.enums.DeliveryServiceType;

@Entity
@Table(name = "deliveries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
	private static final double DISTANCE_RATE = 1000.0; // Rate per km
	private static final double WEIGHT_RATE = 5000.0; // Rate per kg
	private static final double EXPRESS_SURCHARGE = 50000.0;
	private static final double SPECIAL_SURCHARGE = 100000.0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@Column
	private String deliveryNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_location_id")
	@JsonIgnoreProperties("deliveries")
	private Location deliveryLocation;

	@Column(nullable = false)
	private Double estimatedDistance;

	@Column(nullable = false)
	private LocalDateTime estimatedDeliveryTime;

	@Column
	private LocalDateTime actualDeliveryTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id")
	@JsonIgnoreProperties("deliveries")
	private Driver driver;

	@OneToOne(mappedBy = "delivery")
	@JsonIgnoreProperties("delivery")
	private Order order;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeliveryServiceType serviceType;

	@OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL)
	private DeliveryFee deliveryFee;

	@Column(length = 500)
	private List<Long> warehouseList;

	@OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<DeliveryStatusHistory> statusHistory = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		status = DeliveryStatus.PENDING;
	}

	public DeliveryFee calculateDeliveryFee() {
		double baseFee = calculateBaseFee();
		double weightFee = calculateWeightFee();
		double surcharge = calculateSurcharge();

		if (deliveryFee == null) {
			deliveryFee = new DeliveryFee();
			deliveryFee.setDelivery(this);
		}

		deliveryFee.setBaseFee(baseFee);
		deliveryFee.setWeightFee(weightFee);
		deliveryFee.setSurcharge(surcharge);

		return deliveryFee;
	}

	private double calculateBaseFee() { return estimatedDistance * DISTANCE_RATE; }

	private double calculateWeightFee() {
		if (order != null) {
			return order.getTotalWeight() * WEIGHT_RATE;
		}
		return 0.0;
	}

	private double calculateSurcharge() {
		double surcharge = 0.0;

		switch (serviceType) {
		case EXPRESS:
			surcharge += EXPRESS_SURCHARGE;
			break;
		case SPECIAL:
			surcharge += SPECIAL_SURCHARGE;
			break;
		default:
			break;
		}

		return surcharge;
	}

	public void addStatusHistory(DeliveryStatusHistory history) {
		if (statusHistory == null) {
			statusHistory = new ArrayList<>();
		}
		history.setDelivery(this);
		statusHistory.add(history);
	}
}