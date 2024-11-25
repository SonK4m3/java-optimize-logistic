package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.InventoryStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Integer minQuantity;

	@Column(nullable = false)
	private Integer maxQuantity;

	private String location;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InventoryStatus status;

	@Column(nullable = false)
	private LocalDateTime lastCheckedAt;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		lastCheckedAt = LocalDateTime.now();
		status = status == null ? InventoryStatus.ACTIVE : status;
	}

	@PreUpdate
	protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}