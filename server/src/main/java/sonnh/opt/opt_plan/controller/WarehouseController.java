package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
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
import sonnh.opt.opt_plan.payload.dto.WarehouseSpaceDTO;

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
		WarehouseDTO warehouse = warehouseService.createWarehouse(request);
		return ResponseEntity.ok(ApiResponse.success(warehouse));
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

	@Operation(summary = "Update warehouse manager")
	@PatchMapping("/{id}/manager")
	public ResponseEntity<ApiResponse<WarehouseDTO>> updateManager(@PathVariable Long id,
			@RequestParam Long managerId) {
		WarehouseDTO warehouse = warehouseService.updateManager(id, managerId);
		return ResponseEntity.ok(ApiResponse.success(warehouse));
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
		WarehouseReceiptDTO receipt = warehouseService.createReceipt(warehouseId,
				request);
		return ResponseEntity.ok(ApiResponse.success(receipt));
	}

	@Operation(summary = "Confirm receipt", description = "Confirms and processes a warehouse receipt")
	@PutMapping("/receipts/{receiptId}/confirm")
	public ResponseEntity<ApiResponse<?>> confirmReceipt(@PathVariable Long receiptId) {
		try {
			WarehouseReceiptDTO receipt = warehouseService.confirmReceipt(receiptId);
			return ResponseEntity.ok(ApiResponse.success(receipt));
		} catch (BadRequestException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponse.error(e.getMessage()));
		}
	}

	@Operation(summary = "Reject receipt", description = "Rejects and cancels a warehouse receipt")
	@PutMapping("/receipts/{receiptId}/reject")
	public ResponseEntity<ApiResponse<WarehouseReceiptDTO>> rejectReceipt(
			@PathVariable Long receiptId) {
		WarehouseReceiptDTO receipt = warehouseService.rejectReceipt(receiptId);
		return ResponseEntity.ok(ApiResponse.success(receipt));
	}

	@Operation(summary = "Get all receipts", description = "Retrieves all warehouse receipts")
	@GetMapping("/receipts")
	public ResponseEntity<ApiResponse<PageResponse<WarehouseReceiptDTO>>> getAllReceipts(
			@RequestParam(required = false) Long storageLocationId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		PageResponse<WarehouseReceiptDTO> receipts = warehouseService
				.getAllReceipts(storageLocationId, page, size);
		return ResponseEntity.ok(ApiResponse.success(receipts));
	}

	@Operation(summary = "Check warehouse space", description = "Retrieves detailed information about warehouse space utilization")
	@GetMapping("/{warehouseId}/space")
	public ResponseEntity<ApiResponse<WarehouseSpaceDTO>> checkWarehouseSpace(
			@PathVariable Long warehouseId) {
		WarehouseSpaceDTO spaceInfo = warehouseService.checkWarehouseSpace(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(spaceInfo));
	}

	@Operation(summary = "Get warehouse by ID", description = "Retrieves warehouse details by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<WarehouseDTO>> getWarehouseById(
			@PathVariable Long id) {
		WarehouseDTO warehouse = warehouseService.getWarehouseById(id);
		return ResponseEntity.ok(ApiResponse.success(warehouse));
	}
}