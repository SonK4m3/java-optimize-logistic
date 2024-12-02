package sonnh.opt.opt_plan.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

@Entity
@Table(name = "delivery_assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id", nullable = false)
	private Delivery delivery;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id", nullable = false)
	private Driver driver;

	private List<Long> warehouseIds;

	@Column(nullable = false)
	private LocalDateTime assignedAt;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@Column
	private String rejectionReason;

	@Column
	private LocalDateTime respondedAt;

	@Column
	private LocalDateTime expiresAt;
}