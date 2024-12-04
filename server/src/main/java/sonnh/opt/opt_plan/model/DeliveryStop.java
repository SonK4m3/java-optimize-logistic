package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.DeliveryStopStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_stops")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@Column(nullable = false)
	private Integer sequenceNumber;

	@Column(nullable = false)
	private String stopType; // WAREHOUSE or CUSTOMER

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;

	@Column(nullable = false)
	private LocalDateTime plannedArrival;

	private LocalDateTime actualArrival;

	@Enumerated(EnumType.STRING)
	private DeliveryStopStatus status;
}