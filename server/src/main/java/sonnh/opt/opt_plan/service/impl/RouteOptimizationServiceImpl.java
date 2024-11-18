package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Route;
import sonnh.opt.opt_plan.model.Vehicle;
import sonnh.opt.opt_plan.repository.RouteRepository;
import sonnh.opt.opt_plan.service.RouteOptimizationService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteOptimizationServiceImpl implements RouteOptimizationService {

	private final RouteRepository routeRepository;

	@Override
	public List<Route> optimizeRoutes(List<Delivery> deliveries, List<Vehicle> vehicles) {
		/*
		 * Input: List of deliveries and available vehicles Output: Optimized
		 * routes assigned to vehicles Algorithm: Capacity-based Vehicle Routing
		 * Problem (CVRP)
		 */

		// Validate inputs
		if (deliveries == null || vehicles == null || deliveries.isEmpty()
				|| vehicles.isEmpty()) {
			return Collections.emptyList();
		}

		List<Route> optimizedRoutes = new ArrayList<>();

		// Sort deliveries by weight for better capacity utilization
		List<Delivery> sortedDeliveries = new ArrayList<>(deliveries);
		sortedDeliveries.sort((d1, d2) -> Double.compare(d2.getWeight(), d1.getWeight()));

		// Sort vehicles by capacity
		List<Vehicle> sortedVehicles = new ArrayList<>(vehicles);
		sortedVehicles
				.sort((v1, v2) -> Double.compare(v2.getCapacity(), v1.getCapacity()));

		Map<Vehicle, List<Delivery>> vehicleAssignments = assignDeliveriesToVehicles(
				sortedDeliveries, sortedVehicles);

		// Create routes for each vehicle assignment
		for (Map.Entry<Vehicle, List<Delivery>> entry : vehicleAssignments.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				Route route = createOptimizedRoute(entry.getKey(), entry.getValue());
				optimizedRoutes.add(route);
			}
		}

		return routeRepository.saveAll(optimizedRoutes);
	}

	@Override
	public List<Route> reoptimizeRoutes(List<Route> currentRoutes, Delivery newDelivery) {
		/*
		 * Input: Current routes and new delivery to be inserted Output:
		 * Re-optimized routes including the new delivery Strategy: Find best
		 * insertion point that minimizes additional distance
		 */

		if (currentRoutes == null || currentRoutes.isEmpty()) {
			return Collections.emptyList();
		}

		Route bestRoute = null;
		double minAdditionalCost = Double.MAX_VALUE;

		// Find the best route and position to insert the new delivery
		for (Route route : currentRoutes) {
			if (canAddDeliveryToRoute(route, newDelivery)) {
				double additionalCost = calculateAdditionalCost(route, newDelivery);
				if (additionalCost < minAdditionalCost) {
					minAdditionalCost = additionalCost;
					bestRoute = route;
				}
			}
		}

		if (bestRoute != null) {
			// Insert delivery into best route
			insertDeliveryIntoRoute(bestRoute, newDelivery);
			routeRepository.save(bestRoute);
		} else {
			// Create new route if no existing route can accommodate the
			// delivery
			log.warn("Could not find suitable existing route for delivery ID: {}",
					newDelivery.getId());
		}

		return getCurrentRoutes();
	}

	@Override
	public List<Route> getCurrentRoutes() { return routeRepository.findActiveRoutes(); }

	// Helper methods
	private Map<Vehicle, List<Delivery>> assignDeliveriesToVehicles(
			List<Delivery> deliveries, List<Vehicle> vehicles) {
		Map<Vehicle, List<Delivery>> assignments = new HashMap<>();
		Map<Vehicle, Double> remainingCapacity = new HashMap<>();

		// Initialize assignments and capacity tracking
		vehicles.forEach(vehicle -> {
			assignments.put(vehicle, new ArrayList<>());
			remainingCapacity.put(vehicle, vehicle.getCapacity());
		});

		// Assign deliveries to vehicles based on capacity
		for (Delivery delivery : deliveries) {
			Vehicle bestVehicle = findBestVehicleForDelivery(delivery, remainingCapacity);
			if (bestVehicle != null) {
				assignments.get(bestVehicle).add(delivery);
				remainingCapacity.put(bestVehicle,
						remainingCapacity.get(bestVehicle) - delivery.getWeight());
			}
		}

		return assignments;
	}

	private Vehicle findBestVehicleForDelivery(Delivery delivery,
			Map<Vehicle, Double> remainingCapacity) {
		Vehicle bestVehicle = null;
		double minRemainingCapacity = Double.MAX_VALUE;

		for (Map.Entry<Vehicle, Double> entry : remainingCapacity.entrySet()) {
			if (entry.getValue() >= delivery.getWeight()
					&& entry.getValue() < minRemainingCapacity) {
				bestVehicle = entry.getKey();
				minRemainingCapacity = entry.getValue();
			}
		}

		return bestVehicle;
	}

	private Route createOptimizedRoute(Vehicle vehicle, List<Delivery> deliveries) {
		// Implementation to create an optimized route for a vehicle and its
		// deliveries
		// ...
		return Route.builder().vehicle(vehicle).build();
	}

	private boolean canAddDeliveryToRoute(Route route, Delivery delivery) {
		// Implementation to check if a delivery can be added to a route
		// ...
		return true;
	}

	private double calculateAdditionalCost(Route route, Delivery delivery) {
		// Implementation to calculate the additional cost of adding a delivery
		// to a route
		// ...
		return 0;
	}

	private void insertDeliveryIntoRoute(Route route, Delivery delivery) {
		// Implementation to insert a delivery into a route
		// ...
	}
}