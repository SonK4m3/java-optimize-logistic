package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String unit; // KG, PCS, BOX...

	@Column(nullable = false)
	private Double price;

	@Enumerated(EnumType.STRING)
	private StorageCondition storageCondition; // NORMAL, COLD, FROZEN,
												// DANGEROUS

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<WarehouseProduct> warehouseProducts;

	@Column(nullable = false)
	private Boolean isActive;

	public static String generateCode(String name) {
		return name.trim().toUpperCase().replaceAll("\\s+", "_");
	}
}