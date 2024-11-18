package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import sonnh.opt.opt_plan.constant.enums.StopStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_stops")
@Data
@Builder
public class DeliveryStop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Route route;

	@ManyToOne
	private Delivery delivery;

	private Integer sequenceNumber; // Thứ tự trong route
	private LocalDateTime plannedArrival;

	@Enumerated(EnumType.STRING)
	private StopStatus status; // PENDING, COMPLETED, FAILED
}