package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orderCode;
	private LocalDateTime orderDate;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	private Double totalAmount;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;

	@OneToOne(mappedBy = "order")
	private Delivery delivery;
}