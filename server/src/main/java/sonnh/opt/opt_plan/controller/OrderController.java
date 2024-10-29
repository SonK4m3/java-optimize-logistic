package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.service.OrderService;
import sonnh.opt.opt_plan.constant.common.Api;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(Api.ORDER_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {
	private final OrderService orderService;

	@Operation(summary = "Create a new order")
	@PostMapping
	public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody Order order) {
		Order createdOrder = orderService.createOrder(order);
		return ResponseEntity.ok(ApiResponse.success("Order created successfully", createdOrder));
	}

	@Operation(summary = "Update order status")
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@PathVariable Long id,
			@RequestParam OrderStatus status) {
		Order updatedOrder = orderService.updateOrderStatus(id, status);
		return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", updatedOrder));
	}

	@Operation(summary = "Get order by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
		Order order = orderService.getOrderById(id);
		return ResponseEntity.ok(ApiResponse.success(order));
	}

	@Operation(summary = "Get orders by status")
	@GetMapping("/status/{status}")
	public ResponseEntity<ApiResponse<List<Order>>> getOrdersByStatus(@PathVariable OrderStatus status) {
		List<Order> orders = orderService.getOrdersByStatus(status);
		return ResponseEntity.ok(ApiResponse.success(orders));
	}

	@Operation(summary = "Get orders by date range")
	@GetMapping("/date-range")
	public ResponseEntity<ApiResponse<List<Order>>> getOrdersByDateRange(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
		return ResponseEntity.ok(ApiResponse.success(orders));
	}

	@Operation(summary = "Get orders by user")
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUser(@PathVariable Long userId) {
		List<Order> orders = orderService.getOrdersByUser(userId);
		return ResponseEntity.ok(ApiResponse.success(orders));
	}

	@Operation(summary = "Delete an order")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
	}
}