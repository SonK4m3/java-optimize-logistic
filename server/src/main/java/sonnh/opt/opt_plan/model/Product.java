package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

	@Column(nullable = false)
	private String name;

	private String description;
	private String sku;

	@Column(nullable = false)
	private Double price;

	private Double weight;
	private String dimensions; // Format: LxWxH
	private Boolean isFragile;
	private Boolean isDangerous;
	private Boolean requiresRefrigeration;

	@Column(nullable = false)
	private Integer minStockLevel;

	@Column(nullable = false)
	private Integer maxStockLevel;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product")
	private List<OrderDetail> orderDetails;

	@OneToMany(mappedBy = "product")
	private List<Inventory> inventories;

	private String manufacturer;
	private String origin;
	private String unit; // e.g., pieces, kg, liters
	private String shelfLife;
	private String storageInstructions;

	@CreationTimestamp
	private LocalDateTime createdAt;
	private String createdBy;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	private String updatedBy;
	private Boolean isActive;
}