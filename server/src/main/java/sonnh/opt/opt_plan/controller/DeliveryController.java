package sonnh.opt.opt_plan.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.DeliveryDTO;
import sonnh.opt.opt_plan.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Api.DELIVERY_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Delivery Management", description = "APIs for managing deliveries")
public class DeliveryController {
	private final OrderService orderService;

	@Operation(summary = "In transit delivery")
	@GetMapping("/{driverId}/in-transit")
	public ResponseEntity<ApiResponse<List<DeliveryDTO>>> inTransitDelivery(
			@PathVariable Long driverId) {
		List<DeliveryDTO> deliveries = orderService.inTransitDelivery(driverId);
		return ResponseEntity.ok(ApiResponse.success("Delivery in transit", deliveries));
	}

	@Operation(summary = "Complete delivery")
	@PostMapping("/{driverId}/complete-delivery/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryDTO>> completeDelivery(
			@PathVariable Long driverId, @PathVariable Long deliveryId) {
		DeliveryDTO delivery = orderService.completeDelivery(driverId, deliveryId);
		return ResponseEntity.ok(ApiResponse.success("Delivery completed", delivery));
	}

	@Operation(summary = "Cancel delivery")
	@PostMapping("/{driverId}/reject-delivery/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryDTO>> cancelDelivery(
			@PathVariable Long driverId, @PathVariable Long deliveryId) {
		DeliveryDTO delivery = orderService.cancelDelivery(driverId, deliveryId);
		return ResponseEntity.ok(ApiResponse.success("Delivery canceled", delivery));
	}
}
