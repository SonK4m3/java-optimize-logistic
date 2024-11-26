package sonnh.opt.opt_plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import sonnh.opt.opt_plan.payload.request.CreateShipmentRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.service.ShipmentService;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.ShipmentDTO;
import sonnh.opt.opt_plan.model.Shipment;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.utils.SecurityUtils;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {
	private final ShipmentService shipmentService;
	private final SecurityUtils securityUtils;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<ShipmentDTO>> createShipment(
			@RequestBody @Valid CreateShipmentRequest request) {
		Optional<User> user = securityUtils.getCurrentUser();

		if (user.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
		}

		Shipment shipment = shipmentService.createShipment(request, user.get().getId());
		return ResponseEntity.ok(ApiResponse.success("Shipment created successfully",
				ShipmentDTO.fromEntity(shipment)));
	}

	@PutMapping("/{id}/confirm")
	public ResponseEntity<ApiResponse<ShipmentDTO>> confirmShipment(
			@PathVariable Long id) {
		Optional<User> user = securityUtils.getCurrentUser();

		if (user.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
		}

		Shipment shipment;
		try {
			shipment = shipmentService.confirmShipment(id, user.get().getId());
		} catch (BadRequestException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
		return ResponseEntity.ok(ApiResponse.success("Shipment confirmed successfully",
				ShipmentDTO.fromEntity(shipment)));
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<ApiResponse<ShipmentDTO>> cancelShipment(
			@PathVariable Long id) {
		Optional<User> user = securityUtils.getCurrentUser();

		if (user.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
		}

		Shipment shipment;
		try {
			shipment = shipmentService.cancelShipment(id, user.get().getId());
		} catch (BadRequestException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
		}
		return ResponseEntity.ok(ApiResponse.success("Shipment cancelled successfully",
				ShipmentDTO.fromEntity(shipment)));
	}

	@GetMapping("/")
	public ResponseEntity<ApiResponse<PageResponse<ShipmentDTO>>> getShipments(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page - 1, size,
				Sort.by("createdAt").descending());
		Page<Shipment> shipments = shipmentService.getShipments(pageable);
		PageResponse<ShipmentDTO> response = PageResponse.of(shipments,
				ShipmentDTO::fromEntity);

		return ResponseEntity
				.ok(ApiResponse.success("Shipments fetched successfully", response));
	}
}