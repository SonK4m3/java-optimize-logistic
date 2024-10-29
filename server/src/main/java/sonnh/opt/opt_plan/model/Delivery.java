package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String deliveryCode;
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;
	private LocalDateTime deliveryDate;
	private String address;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;
}