package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.service.InventoryService;
import sonnh.opt.opt_plan.constant.common.Api;
import java.util.List;

@RestController
@RequestMapping(Api.INVENTORY_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing inventory")
public class InventoryController {
	private final InventoryService inventoryService;

	@Operation(summary = "Create a new inventory record")
	@PostMapping
	public ResponseEntity<ApiResponse<Inventory>> createInventory(
			@Valid @RequestBody Inventory inventory) {
		Inventory createdInventory = inventoryService.createInventory(inventory);
		return ResponseEntity.ok(
				ApiResponse.success("Inventory created successfully", createdInventory));
	}

	@Operation(summary = "Get inventory by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Inventory>> getInventoryById(
			@PathVariable Long id) {
		Inventory inventory = inventoryService.getInventoryById(id);
		return ResponseEntity.ok(ApiResponse.success(inventory));
	}

	@Operation(summary = "Get all inventory records")
	@GetMapping
	public ResponseEntity<ApiResponse<List<Inventory>>> getAllInventories() {
		List<Inventory> inventories = inventoryService.getAllInventories();
		return ResponseEntity.ok(ApiResponse.success(inventories));
	}
}