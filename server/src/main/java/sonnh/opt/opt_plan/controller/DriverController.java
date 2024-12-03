package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;

import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.payload.request.DriverCreateByManagerRequest;
import sonnh.opt.opt_plan.service.DriverService;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.repository.UserRepository;

@RestController
@RequestMapping(Api.DRIVER_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Driver Management", description = "APIs for managing drivers")
public class DriverController {
	private final DriverService driverService;
	private final UserRepository userRepository;

	@Operation(summary = "Create a new driver")
	@PostMapping
	public ResponseEntity<ApiResponse<DriverDTO>> createDriver(
			@Valid @RequestBody DriverCreateRequest request) {
		DriverDTO driver = DriverDTO.fromEntity(driverService.createDriver(request));
		return ResponseEntity
				.ok(ApiResponse.success("Driver created successfully", driver));
	}

	@Operation(summary = "Create a new driver by manager")
	@PostMapping("/manager")
	public ResponseEntity<ApiResponse<DriverDTO>> createDriverByManager(
			@Valid @RequestBody DriverCreateByManagerRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest()
					.body(ApiResponse.error("Email is already in use"));
		}

		DriverDTO driver = DriverDTO
				.fromEntity(driverService.createDriverByManager(request));
		return ResponseEntity
				.ok(ApiResponse.success("Driver created successfully", driver));
	}

	@Operation(summary = "Update driver location")
	@PatchMapping("/{id}/location")
	public ResponseEntity<ApiResponse<DriverDTO>> updateLocation(@PathVariable Long id,
			@RequestParam Double latitude, @RequestParam Double longitude) {
		DriverDTO driver = DriverDTO
				.fromEntity(driverService.updateDriverLocation(id, latitude, longitude));
		return ResponseEntity
				.ok(ApiResponse.success("Location updated successfully", driver));
	}

	@Operation(summary = "Update driver status")
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<DriverDTO>> updateStatus(@PathVariable Long id,
			@RequestParam DriverStatus status) {
		DriverDTO driver = DriverDTO
				.fromEntity(driverService.updateDriverStatus(id, status));
		return ResponseEntity
				.ok(ApiResponse.success("Status updated successfully", driver));
	}

	@Operation(summary = "Get all drivers")
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<PageResponse<DriverDTO>>> getAllDrivers(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Driver> drivers = driverService.getAllDrivers(page - 1, size);
		PageResponse<DriverDTO> response = PageResponse.of(drivers,
				DriverDTO::fromEntity);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@GetMapping("/available-drivers/{deliveryId}")
	public ResponseEntity<ApiResponse<List<DriverDTO>>> getAvailableDriversForDelivery(
			@PathVariable Long deliveryId) {
		return ResponseEntity
				.ok(ApiResponse.success("Available drivers fetched successfully",
						driverService.getAvailableDriversForDelivery(deliveryId).stream()
								.map(DriverDTO::fromEntity).toList()));
	}
}