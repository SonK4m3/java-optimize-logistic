package sonnh.opt.opt_plan.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.StorageAreaType;

@Entity
@Table(name = "storage_areas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageArea {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StorageAreaType type; // RECEIVING, SHIPPING, STORAGE

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false)
	private Integer currentOccupancy;

	@Column(nullable = false)
	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@OneToMany(mappedBy = "storageArea")
	private List<WarehouseProduct> warehouseProducts;
}