package sonnh.opt.opt_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.service.DeliveryAssignmentService;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.DeliveryAssignmentDTO;
import sonnh.opt.opt_plan.payload.request.DeliveryAssignmentCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/driver/deliveries")
@RequiredArgsConstructor
public class DriverDeliveryController {
	private final DeliveryAssignmentService assignmentService;

	@PostMapping
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> createDeliveryAssignment(
			@RequestBody DeliveryAssignmentCreateRequest request) {
		DeliveryAssignment assignment = assignmentService
				.createDeliveryAssignment(request);
		return ResponseEntity.ok(ApiResponse.success("Delivery assignment created",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@PostMapping("/{deliveryId}/accept")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> acceptDelivery(
			@PathVariable Long deliveryId, @RequestParam Long driverId) {
		DeliveryAssignment assignment = assignmentService.acceptDelivery(deliveryId,
				driverId);
		return ResponseEntity.ok(ApiResponse.success("Delivery accepted",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@PostMapping("/{deliveryId}/reject")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> rejectDelivery(
			@PathVariable Long deliveryId, @RequestParam Long driverId,
			@RequestParam String reason) {
		DeliveryAssignment assignment = assignmentService.rejectDelivery(deliveryId,
				driverId, reason);
		return ResponseEntity.ok(ApiResponse.success("Delivery rejected",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@PostMapping("/{deliveryId}/status")
	public ResponseEntity<ApiResponse<?>> updateStatus(@PathVariable Long deliveryId,
			@RequestParam Long driverId, @RequestParam DeliveryStatus newStatus) {
		assignmentService.updateDeliveryStatus(deliveryId, driverId, newStatus);
		return ResponseEntity.ok(ApiResponse.success("Status updated successfully"));
	}

	@GetMapping("/{driverId}")
	public ResponseEntity<ApiResponse<List<DeliveryAssignmentDTO>>> getByDriver(
			@PathVariable Long driverId) {
		List<DeliveryAssignment> assignments = assignmentService.getByDriver(driverId);
		return ResponseEntity.ok(ApiResponse.success("Assignments fetched",
				assignments.stream().map(DeliveryAssignmentDTO::fromEntity)
						.collect(Collectors.toList())));
	}
}