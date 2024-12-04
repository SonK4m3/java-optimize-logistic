package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VRPRequest {
	@NotNull
	private Location depot;

	@NotEmpty
	@Valid
	private List<Order> orders;

	@NotEmpty
	@Valid
	private List<Vehicle> vehicles;

	private VRPConstraints constraints;

	@Data
	public static class Location {
		private Double latitude;
		private Double longitude;
		private String address;
	}

	@Data
	public static class Order {
		private Long id;
		private Location location;
		private Double demand;
		private Integer serviceTime;
		private String timeWindow;
	}

	@Data
	public static class Vehicle {
		private Long id;
		private Double capacity;
		private Integer maxRouteTime;
		private Integer maxStops;
		private Double costPerKm;
	}

	@Data
	public static class VRPConstraints {
		private Boolean timeWindows = false;
		private Boolean capacityConstraints = true;
		private Boolean balanceRoutes = false;
		private Integer maxTimePerRoute;
		private Integer maxDistancePerRoute;
	}
}
