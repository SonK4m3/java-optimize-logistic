package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.DeliveryServiceType;

import java.util.List;

@Data
public class OrderCreateRequest {
	@NotNull
	private Long customerId;

	private Long customerLocationId;

	@NotEmpty
	private List<OrderItemRequest> items;

	private OrderPriority priority;

	private String deliveryNote;

	private DeliveryServiceType deliveryServiceType;

	@Data
	public static class OrderItemRequest {
		@NotNull
		private Long productId;

		@NotNull
		private Integer quantity;
	}

	@Data
	public static class LocationRequest {
		private String address;
		private Double latitude;
		private Double longitude;
	}
}