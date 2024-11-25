package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.InventoryDTO;
import sonnh.opt.opt_plan.payload.request.InventoryCreateRequest;
import sonnh.opt.opt_plan.service.InventoryService;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.payload.response.PageResponse;

import java.util.List;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping(Api.INVENTORY_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing inventory")
public class InventoryController {
	private final InventoryService inventoryService;

	@Operation(summary = "Create a new inventory record")
	@PostMapping
	public ResponseEntity<ApiResponse<InventoryDTO>> createInventory(
			@Valid @RequestBody InventoryCreateRequest request) {
		Inventory createdInventory = inventoryService.createInventory(request);
		return ResponseEntity
				.ok(ApiResponse.success(InventoryDTO.fromEntity(createdInventory)));
	}

	@Operation(summary = "Get inventory by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InventoryDTO>> getInventoryById(
			@PathVariable Long id) {
		Inventory inventory = inventoryService.getInventoryById(id);
		return ResponseEntity.ok(ApiResponse.success(InventoryDTO.fromEntity(inventory)));
	}

	@Operation(summary = "Get all inventory records")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<InventoryDTO>>> getAllInventories(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		PageRequest pageRequest = PageRequest.of(page - 1, size);
		Page<Inventory> inventories = inventoryService.findAll(pageRequest);
		PageResponse<InventoryDTO> pageResponse = PageResponse
				.of(inventories.map(InventoryDTO::fromEntity));
		return ResponseEntity.ok(ApiResponse.success(pageResponse));
	}

	@Operation(summary = "Get all inventory records by warehouse ID")
	@GetMapping("/warehouse/{warehouseId}")
	public ResponseEntity<ApiResponse<List<InventoryDTO>>> getInventoriesByWarehouseId(
			@PathVariable Long warehouseId) {
		List<Inventory> inventories = inventoryService
				.getInventoriesByWarehouseId(warehouseId);
		return ResponseEntity.ok(ApiResponse
				.success(inventories.stream().map(InventoryDTO::fromEntity).toList()));
	}
}