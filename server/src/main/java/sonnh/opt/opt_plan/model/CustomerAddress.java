package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String country;

	@Column(nullable = false)
	private Boolean isDefault;

	@Column(name = "address_type")
	private String addressType;

	private String recipientInfo;
}