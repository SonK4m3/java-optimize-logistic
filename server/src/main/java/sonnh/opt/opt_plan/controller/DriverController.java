
package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.service.DriverService;
import sonnh.opt.opt_plan.constant.common.Api;
import java.util.List;

@RestController
@RequestMapping(Api.DRIVER_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Driver Management", description = "APIs for managing drivers")
public class DriverController {
	private final DriverService driverService;

	@Operation(summary = "Create a new driver")
	@PostMapping
	public ResponseEntity<ApiResponse<DriverDTO>> createDriver(
			@Valid @RequestBody DriverCreateRequest request) {
		DriverDTO driver = driverService.createDriver(request);
		return ResponseEntity
				.ok(ApiResponse.success("Driver created successfully", driver));
	}

	@Operation(summary = "Update driver location")
	@PatchMapping("/{id}/location")
	public ResponseEntity<ApiResponse<DriverDTO>> updateLocation(@PathVariable Long id,
			@RequestParam Double latitude, @RequestParam Double longitude) {
		DriverDTO driver = driverService.updateDriverLocation(id, latitude, longitude);
		return ResponseEntity
				.ok(ApiResponse.success("Location updated successfully", driver));
	}

	@Operation(summary = "Update driver status")
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<DriverDTO>> updateStatus(@PathVariable Long id,
			@RequestParam DriverStatus status) {
		DriverDTO driver = driverService.updateDriverStatus(id, status);
		return ResponseEntity
				.ok(ApiResponse.success("Status updated successfully", driver));
	}

	@Operation(summary = "Get available drivers nearby")
	@GetMapping("/nearby")
	public ResponseEntity<ApiResponse<List<DriverDTO>>> getAvailableDriversNearby(
			@RequestParam Double latitude, @RequestParam Double longitude,
			@RequestParam Double radius) {
		List<DriverDTO> drivers = driverService.getAvailableDriversNearby(latitude,
				longitude, radius);
		return ResponseEntity.ok(ApiResponse.success(drivers));
	}

	@Operation(summary = "Get drivers by status")
	@GetMapping("/status/{status}")
	public ResponseEntity<ApiResponse<List<DriverDTO>>> getDriversByStatus(
			@PathVariable DriverStatus status) {
		List<DriverDTO> drivers = driverService.getDriversByStatus(status);
		return ResponseEntity.ok(ApiResponse.success(drivers));
	}

	@Operation(summary = "Get drivers nearing end of shift")
	@GetMapping("/ending-shift")
	public ResponseEntity<ApiResponse<List<DriverDTO>>> getDriversNearingEndOfShift(
			@RequestParam(defaultValue = "30") Integer minutes) {
		List<DriverDTO> drivers = driverService.getDriversNearingEndOfShift(minutes);
		return ResponseEntity.ok(ApiResponse.success(drivers));
	}

	@Operation(summary = "Get all drivers")
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<DriverDTO>>> getAllDrivers() {
		List<DriverDTO> drivers = driverService.getAllDrivers();
		return ResponseEntity.ok(ApiResponse.success(drivers));
	}
}