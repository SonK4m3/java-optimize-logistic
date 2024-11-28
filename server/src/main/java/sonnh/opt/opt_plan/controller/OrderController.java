package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import sonnh.opt.opt_plan.model.Order;
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
		OrderDTO createdOrder = OrderDTO
				.fromEntity(orderService.createOrder(orderRequest));
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

		Page<Order> orders = orderService.getOrdersByUser(pageParams);
		PageResponse<OrderDTO> pageResponse = PageResponse.of(orders,
				OrderDTO::fromEntity);
		return ResponseEntity.ok(ApiResponse.success(pageResponse));
	}

	@Operation(summary = "Get order by id")
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
		List<OrderDTO> orders = orderService.getAllOrders().stream()
				.map(OrderDTO::fromEntity).toList();
		return ResponseEntity.ok(ApiResponse.success(orders));
	}
}