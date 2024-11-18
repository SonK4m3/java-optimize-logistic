package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.CargoType;
import sonnh.opt.opt_plan.constant.enums.PayerType;
import sonnh.opt.opt_plan.constant.enums.PickupTimeType;
import sonnh.opt.opt_plan.constant.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders", indexes = {
		@Index(name = "idx_user_created_at", columnList = "user_id,created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
		"hibernateLazyInitializer", "handler"
})
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String orderCode;

	@Column(nullable = false)
	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Enumerated(EnumType.STRING)
	private OrderPriority priority;

	@Enumerated(EnumType.STRING)
	private CargoType cargoType;

	@Enumerated(EnumType.STRING)
	private PayerType payer;

	@Enumerated(EnumType.STRING)
	private ServiceType serviceType;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	// Physical characteristics
	private Double weight;
	private Double totalPrice;
	// Receiver information
	private String receiverName;
	private String receiverPhone;
	private String receiverAddress;
	private double receiverLatitude;
	private double receiverLongitude;

	// Pickup details
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pickup_warehouse_id")
	private Warehouse pickupWarehouse;

	@Enumerated(EnumType.STRING)
	private PickupTimeType pickupTime;

	// Relationships
	@JsonManagedReference
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderProduct> orderProducts;

	// Tracking
	@Column(nullable = true)
	private LocalDateTime lastUpdated;
	@Column(nullable = true)
	private String lastUpdatedBy;

	// Helper method to add product
	public void addOrderProduct(OrderProduct orderProduct) {
		if (orderProducts == null) {
			orderProducts = new ArrayList<>();
		}
		orderProducts.add(orderProduct);
		orderProduct.setOrder(this);
	}

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() { createdAt = LocalDateTime.now(); }
}