package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "deliveries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@Column
	private String deliveryNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pickup_location_id")
	@JsonIgnoreProperties("deliveries")
	private Location pickupLocation;

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

	@ElementCollection
	@CollectionTable(name = "delivery_status_history", joinColumns = @JoinColumn(name = "delivery_id"))
	private List<DeliveryStatusHistory> statusHistory;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		status = DeliveryStatus.PENDING;
	}

	public void updateStatus(DeliveryStatus newStatus, String note) {
		this.status = newStatus;
		this.updatedAt = LocalDateTime.now();

		if (statusHistory == null) {
			statusHistory = new ArrayList<>();
		}
		statusHistory.add(DeliveryStatusHistory.builder().status(newStatus).note(note)
				.timestamp(LocalDateTime.now()).build());
	}
}