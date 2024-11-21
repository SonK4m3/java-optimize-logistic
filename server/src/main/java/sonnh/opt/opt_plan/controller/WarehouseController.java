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

	@Operation(summary = "Create inbound receipt", description = "Creates a new inbound warehouse receipt")
	@PostMapping("/{warehouseId}/receipts/inbound")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> createInboundReceipt(
			@PathVariable Long warehouseId,
			@Valid @RequestBody ReceiptCreateRequest request) {
		log.info("Creating inbound receipt for warehouse ID: {}", warehouseId);
		WarehouseReceiptDTO receipt = warehouseService.createInboundReceipt(warehouseId,
				request);
		return ResponseEntity
				.ok(ApiResponse.success("Inbound receipt created successfully", receipt));
	}

	@Operation(summary = "Confirm inbound receipt", description = "Confirms and processes an inbound warehouse receipt")
	@PutMapping("/receipts/{receiptId}/confirm-inbound")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> confirmInboundReceipt(
			@PathVariable Long receiptId) {
		log.info("Confirming inbound receipt ID: {}", receiptId);
		WarehouseReceiptDTO receipt = warehouseService.confirmInboundReceipt(receiptId);
		return ResponseEntity.ok(
				ApiResponse.success("Inbound receipt confirmed successfully", receipt));
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

	@Operation(summary = "Create outbound receipt", description = "Creates a new outbound warehouse receipt")
	@PostMapping("/{warehouseId}/receipts/outbound")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> createOutboundReceipt(
			@PathVariable Long warehouseId,
			@Valid @RequestBody ReceiptCreateRequest request) {
		log.info("Creating outbound receipt for warehouse ID: {}", warehouseId);
		WarehouseReceiptDTO receipt = warehouseService.createOutboundReceipt(warehouseId,
				request);
		return ResponseEntity.ok(
				ApiResponse.success("Outbound receipt created successfully", receipt));
	}

	@Operation(summary = "Confirm outbound receipt", description = "Confirms and processes an outbound warehouse receipt")
	@PutMapping("/receipts/{receiptId}/confirm-outbound")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> confirmOutboundReceipt(
			@PathVariable Long receiptId) {
		log.info("Confirming outbound receipt ID: {}", receiptId);
		WarehouseReceiptDTO receipt = warehouseService.confirmOutboundReceipt(receiptId);
		return ResponseEntity.ok(
				ApiResponse.success("Outbound receipt confirmed successfully", receipt));
	}
}