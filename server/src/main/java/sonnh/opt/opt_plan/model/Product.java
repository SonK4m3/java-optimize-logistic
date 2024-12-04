package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sonnh.opt.opt_plan.constant.enums.ProductStatus;
import sonnh.opt.opt_plan.constant.enums.StorageCondition;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {
		"inventories", "category", "supplier"
})
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

	@Column(nullable = false)
	private Double weight; // Khối lượng đơn vị

	private String dimensions; // Kích thước đóng gói

	@Column(nullable = false)
	private Integer minStockLevel; // Mức tồn kho tối thiểu

	@Column(nullable = false)
	private Integer maxStockLevel; // Mức tồn kho tối đa

	@Column(nullable = false)
	private Integer reorderPoint; // Điểm đặt hàng lại

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StorageCondition storageCondition;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@JsonIgnoreProperties("products")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	@JsonIgnoreProperties("products")
	private Supplier supplier;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductInventory> inventories;

	@Column(nullable = true)
	private String imageUrl;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		if (status == null) {
			status = ProductStatus.ACTIVE;
		}
	}

	@PreUpdate
	protected void onUpdate() { updatedAt = LocalDateTime.now(); }

	public static String generateCode(String name) {
		return name.substring(0, 3).toUpperCase() + "-"
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
}