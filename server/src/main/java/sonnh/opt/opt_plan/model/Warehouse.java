package sonnh.opt.opt_plan.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.WarehouseType;

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

	@Column(nullable = false)
	private String address;

	private String phone;
	private String email;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private Boolean isActive;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false)
	private Integer currentOccupancy;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	private User manager;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ManyToMany
	@JoinTable(name = "warehouse_product", joinColumns = @JoinColumn(name = "warehouse_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> products;

	@OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
	private List<WarehouseProduct> warehouseProducts;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WarehouseType type;

	@Column(nullable = false)
	private Double area;

	@OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
	private List<StorageArea> storageAreas;

	@OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
	private List<WarehouseReceipt> warehouseReceipts;

	// Helper method to add product
	public void addProduct(Product product, int quantity, String location) {
		if (warehouseProducts == null) {
			warehouseProducts = new ArrayList<>();
		}

		WarehouseProduct warehouseProduct = WarehouseProduct.builder().warehouse(this)
				.product(product).quantity(quantity).location(location).build();

		warehouseProducts.add(warehouseProduct);
	}
}