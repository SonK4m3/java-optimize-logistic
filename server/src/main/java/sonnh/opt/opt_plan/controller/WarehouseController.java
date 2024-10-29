package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse Management", description = "APIs for managing warehouses")
public class WarehouseController {
	private final WarehouseService warehouseService;

	@Operation(summary = "Create a new warehouse")
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Warehouse>> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
		Warehouse createdWarehouse = warehouseService.createWarehouse(warehouse);
		return ResponseEntity.ok(ApiResponse.success("Warehouse created successfully", createdWarehouse));
	}

	@Operation(summary = "Update an existing warehouse")
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Warehouse>> updateWarehouse(@PathVariable Long id,
			@Valid @RequestBody Warehouse warehouse) {
		Warehouse updatedWarehouse = warehouseService.updateWarehouse(id, warehouse);
		return ResponseEntity.ok(ApiResponse.success("Warehouse updated successfully", updatedWarehouse));
	}

	@Operation(summary = "Get warehouse by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Warehouse>> getWarehouseById(@PathVariable Long id) {
		Warehouse warehouse = warehouseService.getWarehouseById(id);
		return ResponseEntity.ok(ApiResponse.success(warehouse));
	}

	@Operation(summary = "Get all warehouses")
	@GetMapping
	public ResponseEntity<ApiResponse<List<Warehouse>>> getAllWarehouses() {
		List<Warehouse> warehouses = warehouseService.getAllWarehouses();
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get active warehouses")
	@GetMapping("/active")
	public ResponseEntity<ApiResponse<List<Warehouse>>> getActiveWarehouses() {
		List<Warehouse> warehouses = warehouseService.getActiveWarehouses();
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Delete a warehouse")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
		warehouseService.deleteWarehouse(id);
		return ResponseEntity.ok(ApiResponse.success("Warehouse deleted successfully", null));
	}
}