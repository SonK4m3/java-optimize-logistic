
package sonnh.opt.opt_plan.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VRPSimpleRequest {
	private List<Depot> depots;
	private List<Customer> customers;
	private List<Vehicle> vehicles;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Location {
		private int id;
		private double x;
		private double y;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Depot {
		private long id;
		private Location location;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Customer {
		private long id;
		private Location location;
		private int demand;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Vehicle {
		private long id;
		private int capacity;
		private List<Customer> customers;
		private Depot depot;
	}
}
