package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Route;
import sonnh.opt.opt_plan.model.Vehicle;
import sonnh.opt.opt_plan.service.DeliveryService;
import sonnh.opt.opt_plan.service.RouteOptimizationService;
import sonnh.opt.opt_plan.service.VehicleService;
import sonnh.opt.opt_plan.payload.dto.RouteDTO;
import sonnh.opt.opt_plan.payload.dto.VehicleDTO;

import sonnh.opt.opt_plan.payload.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Route Optimization", description = "APIs for route optimization operations")
public class RouteOptimizationController {
	private final RouteOptimizationService optimizationService;
	private final VehicleService vehicleService;
	private final DeliveryService deliveryService;

	@Operation(summary = "Optimize delivery routes", description = "Optimizes routes for pending deliveries using available vehicles")
	@PostMapping("/optimize")
	public ResponseEntity<ApiResponse<List<RouteDTO>>> optimizeRoutes() {
		try {
			// 1. Get available vehicles and convert from DTO
			List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles()
					.stream().map(this::convertToVehicle).collect(Collectors.toList());

			// 2. Get pending deliveries
			List<Delivery> pendingDeliveries = new ArrayList<>();

			// 3. Perform optimization
			List<Route> optimizedRoutes = optimizationService
					.optimizeRoutes(pendingDeliveries, availableVehicles);

			List<RouteDTO> optimizedRoutesDTO = optimizedRoutes.stream()
					.map(RouteDTO::fromEntity).collect(Collectors.toList());

			return ResponseEntity.ok(ApiResponse.success("Routes optimized successfully",
					optimizedRoutesDTO));

		} catch (Exception e) {
			log.error("Optimization failed", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Re-optimize routes with new delivery", description = "Re-optimizes existing routes when adding a new delivery")
	@PostMapping("/reoptimize")
	public ResponseEntity<List<Route>> reoptimizeRoutes(
			@Parameter(description = "New delivery to add to routes", required = true) @RequestBody Delivery newDelivery) {
		try {
			// 1. Get current routes
			List<Route> currentRoutes = optimizationService.getCurrentRoutes();

			// 2. Re-optimize with new delivery
			List<Route> updatedRoutes = optimizationService
					.reoptimizeRoutes(currentRoutes, newDelivery);

			return ResponseEntity.ok(updatedRoutes);

		} catch (Exception e) {
			log.error("Reoptimization failed", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Helper method to convert VehicleDTO to Vehicle entity
	 */
	private Vehicle convertToVehicle(VehicleDTO dto) {
		return Vehicle.builder().id(dto.getId()).vehicleCode(dto.getVehicleCode())
				.capacity(dto.getCapacity()).costPerKm(dto.getCostPerKm())
				.currentLat(dto.getCurrentLat()).currentLng(dto.getCurrentLng())
				.status(dto.getStatus()).build();
	}
}