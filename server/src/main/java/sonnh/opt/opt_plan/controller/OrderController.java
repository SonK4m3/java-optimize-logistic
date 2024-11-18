package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.DeliveryDTO;
import sonnh.opt.opt_plan.payload.dto.OrderDTO;
import sonnh.opt.opt_plan.payload.request.OrderCreateRequest;
import sonnh.opt.opt_plan.payload.request.PageParams;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.service.OrderService;
import sonnh.opt.opt_plan.constant.common.Api;
import jakarta.validation.Valid;

@RestController
@RequestMapping(Api.ORDER_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {
	private final OrderService orderService;

	@Operation(summary = "Create a new order")
	@PostMapping
	public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
			@Valid @RequestBody OrderCreateRequest orderRequest) {
		OrderDTO createdOrder = orderService.createOrder(orderRequest);
		return ResponseEntity
				.ok(ApiResponse.success("Order created successfully", createdOrder));
	}

	@Operation(summary = "Get orders by user")
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getOrdersByUser(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		PageParams pageParams = new PageParams();
		pageParams.setPage(page);
		pageParams.setLimit(limit);
		pageParams.setSortBy(sortBy);
		pageParams.setSortDir(sortDir);

		PageResponse<OrderDTO> orders = orderService.getOrdersByUser(pageParams);
		return ResponseEntity.ok(ApiResponse.success(orders));
	}

	@Operation(summary = "Get order by id")
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
		List<OrderDTO> orders = orderService.getAllOrders();
		return ResponseEntity.ok(ApiResponse.success(orders));
	}

	@Operation(summary = "Get order by warehouse")
	@GetMapping("/warehouse/{warehouseId}")
	public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrderByWarehouse(
			@PathVariable Long warehouseId) {
		List<OrderDTO> orders = orderService.getOrdersByWarehouse(warehouseId);
		return ResponseEntity.ok(ApiResponse.success(orders));
	}

	@Operation(summary = "Accept order for delivery")
	@PostMapping("/{driverId}/accept-order/{orderId}")
	public ResponseEntity<ApiResponse<DeliveryDTO>> acceptOrderForDelivery(
			@PathVariable Long driverId, @PathVariable Long orderId) {

		// Assign order to driver and create delivery
		DeliveryDTO delivery = orderService.acceptOrderForDelivery(driverId, orderId);

		return ResponseEntity
				.ok(ApiResponse.success("Order accepted for delivery", delivery));
	}

	@Operation(summary = "Deny order for delivery")
	@PostMapping("/{driverId}/reject-order/{orderId}")
	public ResponseEntity<ApiResponse<DeliveryDTO>> denyOrderForDelivery(
			@PathVariable Long driverId, @PathVariable Long orderId) {
		DeliveryDTO delivery = orderService.denyOrderForDelivery(driverId, orderId);
		return ResponseEntity
				.ok(ApiResponse.success("Order denied for delivery", delivery));
	}

	@Operation(summary = "Get orders by sender id")
	@GetMapping("/sender/{senderId}")
	public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersBySenderId(
			@PathVariable Long senderId) {
		List<OrderDTO> orders = orderService.getOrdersBySenderId(senderId);
		return ResponseEntity.ok(ApiResponse.success(orders));
	}
}