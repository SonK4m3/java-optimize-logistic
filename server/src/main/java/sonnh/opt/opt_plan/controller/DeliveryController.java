package sonnh.opt.opt_plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.DeliveryDTO;
import sonnh.opt.opt_plan.payload.request.DeliveryUpdateRequest;
import sonnh.opt.opt_plan.service.DeliveryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Api.DELIVERY_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Delivery Management", description = "APIs for managing deliveries")
public class DeliveryController {
	private final DeliveryService deliveryService;

	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse<List<DeliveryDTO>>> getDeliveriesByOrderId(
			@PathVariable Long orderId) {
		return ResponseEntity.ok(ApiResponse.success("Deliveries fetched successfully",
				deliveryService.getDeliveriesByOrderId(orderId).stream()
						.map(DeliveryDTO::fromEntity).collect(Collectors.toList())));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<ApiResponse<DeliveryDTO>> updateDeliveryStatus(
			@PathVariable Long id, @Valid @RequestBody DeliveryUpdateRequest request) {
		return ResponseEntity.ok(
				ApiResponse.success("Delivery status updated successfully", DeliveryDTO
						.fromEntity(deliveryService.updateDeliveryStatus(id, request))));
	}

	@PutMapping("/{id}/assign")
	public ResponseEntity<ApiResponse<DeliveryDTO>> assignDriver(@PathVariable Long id,
			@RequestParam Long driverId) {
		return ResponseEntity.ok(ApiResponse.success(
				"Delivery assigned to driver successfully",
				DeliveryDTO.fromEntity(deliveryService.assignDriver(id, driverId))));
	}

}
