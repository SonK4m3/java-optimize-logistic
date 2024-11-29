package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryServiceType;

@Entity
@Table(name = "delivery_fees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@Column(nullable = false)
	private Double baseFee; // Phí cơ bản theo khoảng cách

	@Column(nullable = false)
	private Double weightFee; // Phí theo trọng lượng

	@Column
	private Double surcharge; // Phụ phí (nhiên liệu, vùng xa, etc.)

	@Column(nullable = false)
	private Double totalFee; // Tổng phí = baseFee + weightFee + surcharge

	@PrePersist
	@PreUpdate
	protected void calculateTotalFee() {
		this.totalFee = this.baseFee + this.weightFee
				+ (this.surcharge != null ? this.surcharge : 0.0);
	}
}