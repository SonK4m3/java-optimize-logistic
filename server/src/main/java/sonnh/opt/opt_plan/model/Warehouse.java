package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;
import sonnh.opt.opt_plan.constant.enums.WarehouseStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", nullable = false)
	private Location location;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WarehouseStatus status;

	@Column(nullable = false)
	private Double totalArea;

	@Column(nullable = false)
	private Integer totalCapacity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WarehouseType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Staff manager;

	@OneToMany(mappedBy = "warehouse", cascade = {
			CascadeType.PERSIST, CascadeType.MERGE
	}, orphanRemoval = true)
	@Builder.Default
	private List<StorageArea> storageAreas = new ArrayList<>();

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		if (status == null) {
			status = WarehouseStatus.ACTIVE;
		}
	}

	@PreUpdate
	protected void onUpdate() { updatedAt = LocalDateTime.now(); }

	public static String generateCode() { return "WH" + UUID.randomUUID().toString(); }
}