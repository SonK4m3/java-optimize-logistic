package sonnh.opt.opt_plan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.VehicleDTO;
import sonnh.opt.opt_plan.payload.request.VehicleRequest;
import sonnh.opt.opt_plan.service.VehicleService;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.constant.common.Api;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(Api.VEHICLE_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
@Slf4j
public class VehicleController {
	private final VehicleService vehicleService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<VehicleDTO>>> getAvailableVehicles() {
		return ResponseEntity.ok(ApiResponse.success("Available vehicles",
				vehicleService.getAvailableVehicles()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long id) {
		return vehicleService.getVehicleById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<VehicleDTO> createVehicle(
			@Valid @RequestBody VehicleRequest request) {
		return vehicleService.createVehicle(request).map(ResponseEntity::ok)
				.orElse(ResponseEntity.badRequest().build());
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<VehicleDTO> updateStatus(@PathVariable Long id,
			@RequestParam VehicleStatus status, @RequestParam Double latitude,
			@RequestParam Double longitude) {
		return vehicleService.updateVehicleStatus(id, status, latitude, longitude)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}