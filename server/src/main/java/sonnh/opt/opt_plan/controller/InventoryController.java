package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing inventory")
public class InventoryController {
	private final InventoryService inventoryService;

	@Operation(summary = "Create a new inventory record")
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ApiResponse<Inventory>> createInventory(@Valid @RequestBody Inventory inventory) {
		Inventory createdInventory = inventoryService.createInventory(inventory);
		return ResponseEntity.ok(ApiResponse.success("Inventory created successfully", createdInventory));
	}

	@Operation(summary = "Update an existing inventory record")
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ApiResponse<Inventory>> updateInventory(@PathVariable Long id,
			@Valid @RequestBody Inventory inventory) {
		Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
		return ResponseEntity.ok(ApiResponse.success("Inventory updated successfully", updatedInventory));
	}

	@Operation(summary = "Update inventory quantity")
	@PatchMapping("/{id}/quantity")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<ApiResponse<Inventory>> updateQuantity(@PathVariable Long id,
			@RequestParam Integer quantity) {
		Inventory updatedInventory = inventoryService.updateQuantity(id, quantity);
		return ResponseEntity.ok(ApiResponse.success("Quantity updated successfully", updatedInventory));
	}

	@Operation(summary = "Get inventory by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Inventory>> getInventoryById(@PathVariable Long id) {
		Inventory inventory = inventoryService.getInventoryById(id);
		return ResponseEntity.ok(ApiResponse.success(inventory));
	}

	@Operation(summary = "Get all inventory records")
	@GetMapping
	public ResponseEntity<ApiResponse<List<Inventory>>> getAllInventories() {
		List<Inventory> inventories = inventoryService.getAllInventories();
		return ResponseEntity.ok(ApiResponse.success(inventories));
	}

	@Operation(summary = "Get inventories by warehouse")
	@GetMapping("/warehouse/{warehouseId}")
	public ResponseEntity<ApiResponse<List<Inventory>>> getInventoriesByWarehouse(@PathVariable Long warehouseId) {
		List<Inventory> inventories = inventoryService.getInventoriesByWarehouse(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(inventories));
	}

	@Operation(summary = "Get inventories by product")
	@GetMapping("/product/{productId}")
	public ResponseEntity<ApiResponse<List<Inventory>>> getInventoriesByProduct(@PathVariable Long productId) {
		List<Inventory> inventories = inventoryService.getInventoriesByProduct(productId);
		return ResponseEntity.ok(ApiResponse.success(inventories));
	}

	@Operation(summary = "Get low stock inventories by warehouse")
	@GetMapping("/low-stock/warehouse/{warehouseId}")
	public ResponseEntity<ApiResponse<List<Inventory>>> getLowStockByWarehouse(@PathVariable Long warehouseId) {
		List<Inventory> inventories = inventoryService.getLowStockByWarehouse(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(inventories));
	}

	@Operation(summary = "Delete an inventory record")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteInventory(@PathVariable Long id) {
		inventoryService.deleteInventory(id);
		return ResponseEntity.ok(ApiResponse.success("Inventory deleted successfully", null));
	}
}