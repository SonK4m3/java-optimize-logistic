package sonnh.opt.opt_plan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String address;

	@Column(nullable = false)
	private double latitude;
	@Column(nullable = false)
	private double longitude;

	public double getDistance(Location other) {
		return Math.sqrt(Math.pow(latitude - other.latitude, 2)
				+ Math.pow(longitude - other.longitude, 2));
	}
}
