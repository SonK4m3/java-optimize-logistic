package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "vehicles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String vehicleCode;
	private Double capacity;
	private Double costPerKm;

	private Double currentLat;
	private Double currentLng;

	@Enumerated(EnumType.STRING)
	private VehicleStatus status;

	@OneToMany(mappedBy = "vehicle")
	@ToString.Exclude
	@JsonIgnore
	private List<Route> routes = new ArrayList<>();
}