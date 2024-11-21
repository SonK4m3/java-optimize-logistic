package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import sonnh.opt.opt_plan.constant.enums.StorageCondition;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String barcode;

	@Column(nullable = false)
	private String unit; // KG, PCS, BOX...

	private Double length;
	private Double width;
	private Double height;
	private Double weight;

	@Enumerated(EnumType.STRING)
	private StorageCondition storageCondition; // NORMAL, COLD, FROZEN,
												// DANGEROUS

	@OneToMany(mappedBy = "product")
	private List<WarehouseProduct> warehouseProducts;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(nullable = false)
	private Boolean isActive;
}