package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.constant.enums.StorageAreaType;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.StorageAreaDTO;
import sonnh.opt.opt_plan.payload.request.CreateStorageAreaRequest;
import sonnh.opt.opt_plan.service.StorageAreaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storage-areas")
@RequiredArgsConstructor
@Tag(name = "Storage Area Management", description = "APIs for managing storage areas")
public class StorageAreaController {
	private final StorageAreaService storageAreaService;

	@Operation(summary = "Create new storage area")
	@PostMapping
	public ResponseEntity<ApiResponse<StorageAreaDTO>> createStorageArea(
			@Valid @RequestBody CreateStorageAreaRequest request) {
		StorageAreaDTO createdArea = storageAreaService.createStorageArea(request);
		return ResponseEntity.ok(ApiResponse.success(createdArea));
	}

	@Operation(summary = "Get storage area by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<StorageAreaDTO>> getStorageArea(
			@PathVariable Long id) {
		StorageAreaDTO area = storageAreaService.getStorageAreaById(id);
		return ResponseEntity.ok(ApiResponse.success(area));
	}

	@Operation(summary = "Get all storage areas in warehouse")
	@GetMapping("/warehouse/{warehouseId}")
	public ResponseEntity<ApiResponse<List<StorageAreaDTO>>> getWarehouseAreas(
			@PathVariable Long warehouseId) {
		List<StorageAreaDTO> areas = storageAreaService
				.getStorageAreasByWarehouse(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(areas));
	}

	@Operation(summary = "Get available storage areas")
	@GetMapping("/warehouse/{warehouseId}/available")
	public ResponseEntity<ApiResponse<List<StorageAreaDTO>>> getAvailableAreas(
			@PathVariable Long warehouseId) {
		List<StorageAreaDTO> areas = storageAreaService.getAvailableAreas(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(areas));
	}

	@Operation(summary = "Get storage areas by type")
	@GetMapping("/warehouse/{warehouseId}/type/{type}")
	public ResponseEntity<ApiResponse<List<StorageAreaDTO>>> getAreasByType(
			@PathVariable Long warehouseId, @PathVariable StorageAreaType type) {
		List<StorageAreaDTO> areas = storageAreaService.getAreasByType(warehouseId, type);
		return ResponseEntity.ok(ApiResponse.success(areas));
	}

	@Operation(summary = "Deactivate storage area")
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<ApiResponse<String>> deactivateArea(@PathVariable Long id) {
		storageAreaService.deactivateStorageArea(id);
		return ResponseEntity
				.ok(ApiResponse.success("Deactivated storage area successfully"));
	}
}