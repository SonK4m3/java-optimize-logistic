package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;

import java.util.List;

@Data
public class OrderCreateRequest {
	@NotNull
	private Long customerId;

	@NotEmpty
	private List<OrderItemRequest> items;

	private OrderPriority priority;

	private String deliveryNote;

	private Long pickupLocationId;

	@Data
	public static class OrderItemRequest {
		@NotNull
		private Long productId;

		@NotNull
		private Integer quantity;
	}
}