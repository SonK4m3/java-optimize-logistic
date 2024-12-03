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
import sonnh.opt.opt_plan.constant.common.Api;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Api.DRIVER_DELIVERY_ROUTE)
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

	@PostMapping("/accept")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> acceptDelivery(
			@RequestParam Long deliveryId, @RequestParam Long driverId) {
		DeliveryAssignment assignment = assignmentService.acceptDelivery(deliveryId,
				driverId);
		return ResponseEntity.ok(ApiResponse.success("Delivery accepted",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@PostMapping("/reject")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> rejectDelivery(
			@RequestParam Long deliveryId, @RequestParam Long driverId,
			@RequestParam String reason) {
		DeliveryAssignment assignment = assignmentService.rejectDelivery(deliveryId,
				driverId, reason);
		return ResponseEntity.ok(ApiResponse.success("Delivery rejected",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@PostMapping("/status")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> updateStatus(
			@RequestParam Long deliveryId, @RequestParam Long driverId,
			@RequestParam DeliveryStatus newStatus) {
		DeliveryAssignment assignment = assignmentService.updateStatus(deliveryId,
				driverId, newStatus);
		return ResponseEntity.ok(ApiResponse.success("Status updated",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@GetMapping("/driver/{driverId}")
	public ResponseEntity<ApiResponse<List<DeliveryAssignmentDTO>>> getByDriver(
			@PathVariable Long driverId) {
		List<DeliveryAssignment> assignments = assignmentService.getByDriver(driverId);
		return ResponseEntity.ok(ApiResponse.success("Assignments fetched",
				assignments.stream().map(DeliveryAssignmentDTO::fromEntity)
						.collect(Collectors.toList())));
	}

	@GetMapping("/delivery/{deliveryId}")
	public ResponseEntity<ApiResponse<List<DeliveryAssignmentDTO>>> getByDelivery(
			@PathVariable Long deliveryId) {
		List<DeliveryAssignment> assignments = assignmentService
				.getByDelivery(deliveryId);
		return ResponseEntity.ok(ApiResponse.success("Assignments fetched",
				assignments.stream().map(DeliveryAssignmentDTO::fromEntity)
						.collect(Collectors.toList())));
	}

	@PostMapping("/start/{deliveryAssignmentId}")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> driverStartDelivery(
			@PathVariable Long deliveryAssignmentId) {
		DeliveryAssignment assignment = assignmentService
				.driverStartDelivery(deliveryAssignmentId);
		return ResponseEntity.ok(ApiResponse.success("Delivery started",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@PostMapping("/deliver/{deliveryAssignmentId}")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> driverDeliverDelivery(
			@PathVariable Long deliveryAssignmentId) {
		DeliveryAssignment assignment = assignmentService
				.driverDeliverDelivery(deliveryAssignmentId);
		return ResponseEntity.ok(ApiResponse.success("Delivery delivered",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}

	@GetMapping("/refresh")
	public ResponseEntity<ApiResponse<DeliveryAssignmentDTO>> refreshAssignment(
			@RequestParam Long driverId) {
		DeliveryAssignment assignment = assignmentService.refreshAssignment(driverId);
		return ResponseEntity.ok(ApiResponse.success("Delivery assignment refreshed",
				DeliveryAssignmentDTO.fromEntity(assignment)));
	}
}