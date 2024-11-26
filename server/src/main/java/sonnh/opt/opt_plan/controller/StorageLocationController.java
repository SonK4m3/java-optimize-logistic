package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.StorageLocationDTO;
import sonnh.opt.opt_plan.payload.request.CreateStorageLocationRequest;
import sonnh.opt.opt_plan.payload.request.FindStorageLocationRequest;
import sonnh.opt.opt_plan.service.StorageLocationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storage-locations")
@RequiredArgsConstructor
public class StorageLocationController {
	private final StorageLocationService storageLocationService;

	@Operation(summary = "Create new storage location")
	@PostMapping
	public ResponseEntity<ApiResponse<StorageLocationDTO>> createLocation(
			@RequestBody CreateStorageLocationRequest request) {
		StorageLocationDTO location = storageLocationService
				.createStorageLocation(request);
		return ResponseEntity.ok(ApiResponse.success(location));
	}

	@Operation(summary = "Find available locations based on criteria")
	@PostMapping("/find-available")
	public ResponseEntity<ApiResponse<List<StorageLocationDTO>>> findAvailableLocations(
			@RequestBody FindStorageLocationRequest request) {
		List<StorageLocationDTO> locations = storageLocationService
				.findAvailableLocations(request);
		return ResponseEntity.ok(ApiResponse.success(locations));
	}

	@Operation(summary = "Get locations by warehouse")
	@GetMapping("/warehouse/{warehouseId}")
	public ResponseEntity<ApiResponse<List<StorageLocationDTO>>> getLocationsByWarehouse(
			@PathVariable Long warehouseId) {
		List<StorageLocationDTO> locations = storageLocationService
				.getLocationsByWarehouse(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(locations));
	}
}