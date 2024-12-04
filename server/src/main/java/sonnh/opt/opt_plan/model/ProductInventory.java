package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.InventoryStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing product inventory information in a warehouse Manages
 * stock levels, locations, and inventory movements
 */
@Entity
@Table(name = "product_inventories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storage_area_id", nullable = false)
	private StorageArea storageArea;

	@Column(nullable = false)
	private Integer totalQuantity; // Tổng số lượng

	@Column(nullable = false)
	private Integer availableQuantity; // Số lượng khả dụng

	@Column(nullable = false)
	private Integer reservedQuantity; // Số lượng đã đặt

	@Column(nullable = false)
	private Integer damagedQuantity; // Số lượng hỏng

	private String batchNumber; // Số lô
	private LocalDateTime expiryDate; // Ngày hết hạn

	private String shelfLocation; // Vị trí kệ
	private String binLocation; // Vị trí thùng
	private String zoneCode; // Mã khu vực

	@OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
	private List<InventoryTransaction> transactions;

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
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		lastCheckedAt = now;
		if (status == null) {
			status = InventoryStatus.ACTIVE;
		}
		if (reservedQuantity == null) {
			reservedQuantity = 0;
		}
		if (damagedQuantity == null) {
			damagedQuantity = 0;
		}
		totalQuantity = availableQuantity + reservedQuantity + damagedQuantity;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
		totalQuantity = availableQuantity + reservedQuantity + damagedQuantity;
	}

	public Integer getQuantity() {
		return availableQuantity + reservedQuantity + damagedQuantity;
	}
}