package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String phone;
	private String licenseNumber;

	@Enumerated(EnumType.STRING)
	private DriverStatus status;

	@OneToMany(mappedBy = "driver")
	private List<Delivery> deliveries;
}