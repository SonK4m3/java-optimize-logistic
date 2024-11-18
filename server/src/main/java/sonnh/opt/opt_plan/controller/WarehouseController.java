
package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseUpdateRequest;
import sonnh.opt.opt_plan.service.WarehouseService;
import sonnh.opt.opt_plan.constant.common.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Api.WAREHOUSE_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Warehouse Management", description = "APIs for managing warehouses")
@Slf4j
public class WarehouseController {
	private final WarehouseService warehouseService;

	@Operation(summary = "Create a new warehouse")
	@PostMapping
	public ResponseEntity<ApiResponse<WarehouseDTO>> createWarehouse(
			@Valid @RequestBody WarehouseCreateRequest request) {
		log.info("Creating new warehouse with code: {}", request.getCode());
		WarehouseDTO warehouse = warehouseService.createWarehouse(request);
		return ResponseEntity
				.ok(ApiResponse.success("Warehouse created successfully", warehouse));
	}

	@Operation(summary = "Update warehouse details")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<WarehouseDTO>> updateWarehouse(
			@PathVariable Long id, @Valid @RequestBody WarehouseUpdateRequest request) {
		log.info("Updating warehouse with ID: {}", id);
		WarehouseDTO warehouse = warehouseService.updateWarehouse(id, request);
		return ResponseEntity
				.ok(ApiResponse.success("Warehouse updated successfully", warehouse));
	}

	@Operation(summary = "Get warehouse by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<WarehouseDTO>> getWarehouse(@PathVariable Long id) {
		WarehouseDTO warehouse = warehouseService.getWarehouseById(id);
		return ResponseEntity.ok(ApiResponse.success(warehouse));
	}

	@Operation(summary = "Get warehouse by code")
	@GetMapping("/code/{code}")
	public ResponseEntity<ApiResponse<WarehouseDTO>> getWarehouseByCode(
			@PathVariable String code) {
		WarehouseDTO warehouse = warehouseService.getWarehouseByCode(code);
		return ResponseEntity.ok(ApiResponse.success(warehouse));
	}

	@Operation(summary = "Get all warehouses")
	@GetMapping
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getAllWarehouses() {
		List<WarehouseDTO> warehouses = warehouseService.getAllWarehouses();
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get active warehouses")
	@GetMapping("/active")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getActiveWarehouses() {
		List<WarehouseDTO> warehouses = warehouseService.getActiveWarehouses();
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get nearby warehouses")
	@GetMapping("/nearby")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getNearbyWarehouses(
			@RequestParam Double latitude, @RequestParam Double longitude,
			@RequestParam(defaultValue = "10.0") Double radius) {
		List<WarehouseDTO> warehouses = warehouseService.getNearbyWarehouses(latitude,
				longitude, radius);
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get warehouses with available capacity")
	@GetMapping("/available")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getAvailableWarehouses(
			@RequestParam Integer requiredCapacity) {
		List<WarehouseDTO> warehouses = warehouseService
				.getAvailableWarehouses(requiredCapacity);
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Update warehouse occupancy")
	@PatchMapping("/{id}/occupancy")
	public ResponseEntity<ApiResponse<WarehouseDTO>> updateOccupancy(
			@PathVariable Long id, @RequestParam Integer newOccupancy) {
		log.info("Updating occupancy for warehouse ID: {} to: {}", id, newOccupancy);
		WarehouseDTO warehouse = warehouseService.updateOccupancy(id, newOccupancy);
		return ResponseEntity.ok(ApiResponse
				.success("Warehouse occupancy updated successfully", warehouse));
	}

	@Operation(summary = "Update warehouse active status")
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<WarehouseDTO>> updateActiveStatus(
			@PathVariable Long id, @RequestParam Boolean isActive) {
		log.info("Updating active status for warehouse ID: {} to: {}", id, isActive);
		WarehouseDTO warehouse = warehouseService.updateActiveStatus(id, isActive);
		return ResponseEntity.ok(
				ApiResponse.success("Warehouse status updated successfully", warehouse));
	}

	@Operation(summary = "Update warehouse manager")
	@PatchMapping("/{id}/manager")
	public ResponseEntity<ApiResponse<WarehouseDTO>> updateManager(@PathVariable Long id,
			@RequestParam Long managerId) {
		log.info("Updating manager for warehouse ID: {} to manager ID: {}", id,
				managerId);
		WarehouseDTO warehouse = warehouseService.updateManager(id, managerId);
		return ResponseEntity.ok(
				ApiResponse.success("Warehouse manager updated successfully", warehouse));
	}

	@Operation(summary = "Get warehouses by manager")
	@GetMapping("/manager/{managerId}")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getWarehousesByManager(
			@PathVariable Long managerId) {
		List<WarehouseDTO> warehouses = warehouseService
				.getWarehousesByManager(managerId);
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get high utilization warehouses")
	@GetMapping("/high-utilization")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getHighUtilizationWarehouses(
			@RequestParam(defaultValue = "80.0") Double threshold) {
		List<WarehouseDTO> warehouses = warehouseService
				.getHighUtilizationWarehouses(threshold);
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get low capacity warehouses")
	@GetMapping("/low-capacity")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getLowCapacityWarehouses(
			@RequestParam(defaultValue = "20.0") Double threshold) {
		List<WarehouseDTO> warehouses = warehouseService
				.getLowCapacityWarehouses(threshold);
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get total available capacity")
	@GetMapping("/total-capacity")
	public ResponseEntity<ApiResponse<Integer>> getTotalAvailableCapacity() {
		Integer capacity = warehouseService.getTotalAvailableCapacity();
		return ResponseEntity.ok(ApiResponse.success(capacity));
	}

	@Operation(summary = "Check warehouse capacity availability")
	@GetMapping("/{id}/check-capacity")
	public ResponseEntity<ApiResponse<Boolean>> checkAvailableCapacity(
			@PathVariable Long id, @RequestParam Integer requiredCapacity) {
		Boolean hasCapacity = warehouseService.hasAvailableCapacity(id, requiredCapacity);
		return ResponseEntity.ok(ApiResponse.success(hasCapacity));
	}

	@Operation(summary = "Delete warehouse")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteWarehouse(@PathVariable Long id) {
		log.info("Deleting warehouse with ID: {}", id);
		warehouseService.deleteWarehouse(id);
		return ResponseEntity.ok(ApiResponse.success("Warehouse deleted successfully"));
	}

}