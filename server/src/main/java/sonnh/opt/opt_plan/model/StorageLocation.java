package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.StorageLocationType;

import java.util.List;

@Entity
@Table(name = "storage_locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code; // Mã vị trí (e.g., "RACK-A1-L2")

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storage_area_id", nullable = false)
	private StorageArea storageArea;

	@OneToMany(mappedBy = "storageLocation", cascade = CascadeType.ALL)
	private List<WarehouseReceipt> receipts;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StorageLocationType type;

	@Column(nullable = false)
	private Double length;

	@Column(nullable = false)
	private Double width;

	@Column(nullable = false)
	private Double height;

	@Column(nullable = false)
	private Double maxWeight;

	@Column(nullable = false)
	private Boolean isOccupied;

	// FIFO/LIFO tracking
	@Column(nullable = false)
	private Integer level; // Tầng số mấy

	@Column(nullable = false)
	private Integer position; // Vị trí trong tầng

	public static String generateLocationCode(StorageLocationType type, Integer level,
			Integer position) {
		return String.format("%s-%d-%d", type.name(), level, position);
	}
}