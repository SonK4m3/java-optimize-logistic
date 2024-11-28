package sonnh.opt.opt_plan.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;

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
@ToString(exclude = {
		"customer", "orderDetails"
})
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String orderCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonIgnoreProperties("orders")
	private Customer customer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	@Enumerated(EnumType.STRING)
	private OrderPriority priority;

	@Column(nullable = false)
	private Double totalAmount;

	@Column(nullable = false)
	private Double totalWeight;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<OrderDetail> orderDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	@JsonIgnoreProperties("order")
	private Delivery delivery;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime lastUpdated;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		orderCode = generateOrderCode();
	}

	@PreUpdate
	protected void onUpdate() { lastUpdated = LocalDateTime.now(); }

	// Simplified business methods
	public void addOrderDetail(OrderDetail detail) {
		if (orderDetails == null) {
			orderDetails = new ArrayList<>();
		}
		orderDetails.add(detail);
		detail.setOrder(this);
		recalculateTotals();
	}

	private void recalculateTotals() {
		this.totalAmount = orderDetails.stream()
				.mapToDouble(detail -> detail.getPrice() * detail.getQuantity()).sum();
		this.totalWeight = orderDetails.stream()
				.mapToDouble(detail -> detail.getWeight() * detail.getQuantity()).sum();
	}

	public static String generateOrderCode() {
		return "OD" + LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}
}