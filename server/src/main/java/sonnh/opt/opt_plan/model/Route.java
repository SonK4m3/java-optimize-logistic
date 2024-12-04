package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import sonnh.opt.opt_plan.constant.enums.RouteStatus;

import java.util.List;

@Entity
@Table(name = "routes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicle_id")
	@ToString.Exclude
	@JsonIgnore
	private Vehicle vehicle;

	private Double totalDistance;
	private Double totalCost;

	@Enumerated(EnumType.STRING)
	private RouteStatus status;
}