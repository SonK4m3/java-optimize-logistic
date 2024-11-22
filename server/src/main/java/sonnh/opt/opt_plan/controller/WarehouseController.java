package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.service.WarehouseService;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.InventoryUpdateRequest;
import sonnh.opt.opt_plan.payload.dto.InventoryDTO;

import java.util.List;

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

	@Operation(summary = "Get all warehouses")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<WarehouseDTO>>> getAllWarehouses(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		PageResponse<WarehouseDTO> warehouses = warehouseService.getAllWarehouses(page,
				size);
		return ResponseEntity.ok(ApiResponse.success(warehouses));
	}

	@Operation(summary = "Get active warehouses")
	@GetMapping("/active")
	public ResponseEntity<ApiResponse<List<WarehouseDTO>>> getActiveWarehouses() {
		List<WarehouseDTO> warehouses = warehouseService.getActiveWarehouses();
		return ResponseEntity.ok(ApiResponse.success(warehouses));
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

	@Operation(summary = "Create receipt", description = "Creates a new receipt")
	@PostMapping("/{warehouseId}/receipts")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> createReceipt(
			@PathVariable Long warehouseId,
			@Valid @RequestBody ReceiptCreateRequest request) {
		log.info("Creating receipt for warehouse ID: {}", warehouseId);
		WarehouseReceiptDTO receipt = warehouseService.createReceipt(warehouseId,
				request);
		return ResponseEntity
				.ok(ApiResponse.success("Receipt created successfully", receipt));
	}

	@Operation(summary = "Confirm inbound receipt", description = "Confirms and processes an inbound warehouse receipt")
	@PutMapping("/receipts/{receiptId}/confirm")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> confirmReceipt(
			@PathVariable Long receiptId) {
		log.info("Confirming receipt ID: {}", receiptId);
		WarehouseReceiptDTO receipt = warehouseService.confirmReceipt(receiptId);
		return ResponseEntity
				.ok(ApiResponse.success("Receipt confirmed successfully", receipt));
	}

	@Operation(summary = "Reject inbound receipt", description = "Rejects and cancels an inbound warehouse receipt")
	@PutMapping("/receipts/{receiptId}/reject")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> rejectReceipt(
			@PathVariable Long receiptId) {
		log.info("Rejecting receipt ID: {}", receiptId);
		WarehouseReceiptDTO receipt = warehouseService.rejectReceipt(receiptId);
		return ResponseEntity
				.ok(ApiResponse.success("Receipt rejected successfully", receipt));
	}

	@Operation(summary = "Update inventory", description = "Updates inventory levels for products in warehouse")
	@PutMapping("/{warehouseId}/inventory")
	public ResponseEntity<ApiResponse<List<InventoryDTO>>> updateInventory(
			@PathVariable Long warehouseId,
			@Valid @RequestBody List<InventoryUpdateRequest> updates) {
		log.info("Updating inventory for warehouse ID: {}", warehouseId);
		List<InventoryDTO> updatedInventory = warehouseService
				.updateInventory(warehouseId, updates);
		return ResponseEntity.ok(
				ApiResponse.success("Inventory updated successfully", updatedInventory));
	}

	@Operation(summary = "Get all receipts", description = "Retrieves all warehouse receipts")
	@GetMapping("/receipts")
	public ResponseEntity<ApiResponse<PageResponse<WarehouseReceiptDTO>>> getAllReceipts(
			@RequestParam(required = false) Long warehouseId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		log.info("Retrieving all receipts for warehouse ID: {}", warehouseId);
		PageResponse<WarehouseReceiptDTO> receipts = warehouseService
				.getAllReceipts(warehouseId, page, size);
		return ResponseEntity
				.ok(ApiResponse.success("Receipts retrieved successfully", receipts));
	}
}