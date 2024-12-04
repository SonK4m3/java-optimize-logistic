package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.ShipmentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ShipmentStatus status;

	@Column(name = "shipment_date", nullable = false)
	private LocalDateTime shipmentDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "confirmed_by")
	private User confirmedBy;

	private LocalDateTime confirmedAt;

	@Column(columnDefinition = "text")
	private String notes;

	@OneToMany(mappedBy = "shipment", cascade = {
			CascadeType.PERSIST, CascadeType.MERGE
	}, orphanRemoval = true)
	@Builder.Default
	private List<ShipmentDetail> details = new ArrayList<>();

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		if (status == null) {
			status = ShipmentStatus.PENDING;
		}
	}

	@PreUpdate
	protected void onUpdate() { updatedAt = LocalDateTime.now(); }

	// Helper methods
	public void addDetail(ShipmentDetail detail) {
		details.add(detail);
		detail.setShipment(this);
	}

	public void removeDetail(ShipmentDetail detail) {
		details.remove(detail);
		detail.setShipment(null);
	}

	public static String generateCode() { return "SH" + UUID.randomUUID().toString(); }
}