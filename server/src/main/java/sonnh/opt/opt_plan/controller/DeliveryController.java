package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.DeliveryDTO;
import sonnh.opt.opt_plan.payload.request.SuggestedDriversRequest;
import sonnh.opt.opt_plan.payload.response.SuggestDriversResponse;
import sonnh.opt.opt_plan.service.DeliveryService;

@RestController
@RequestMapping(Api.DELIVERY_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Delivery Management", description = "APIs for managing deliveries")
public class DeliveryController {
	private final DeliveryService deliveryService;

	@GetMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryDTO>> getDeliveryById(
			@PathVariable Long deliveryId) {
		return ResponseEntity.ok(ApiResponse.success("Delivery fetched successfully",
				DeliveryDTO.fromEntity(deliveryService.getDeliveryOrThrow(deliveryId))));
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse<DeliveryDTO>> getDeliveryByOrderId(
			@PathVariable Long orderId) {
		return ResponseEntity.ok(ApiResponse.success("Delivery fetched successfully",
				DeliveryDTO.fromEntity(deliveryService.getDeliveryByOrderId(orderId))));
	}

	@PostMapping("/suggest-drivers")
	public ResponseEntity<ApiResponse<SuggestDriversResponse>> suggestDriversForDelivery(
			@Valid @RequestBody SuggestedDriversRequest request) {
		return ResponseEntity
				.ok(ApiResponse.success("Suggested drivers fetched successfully",
						deliveryService.suggestDriversForDelivery(request.getDeliveryId(),
								request.getDriverNumber(), request.getDriverIds())));
	}
}
