package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String vehicleNumber;
	private String type;
	@Enumerated(EnumType.STRING)
	private VehicleStatus status;
	private Double capacity;

	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;
}