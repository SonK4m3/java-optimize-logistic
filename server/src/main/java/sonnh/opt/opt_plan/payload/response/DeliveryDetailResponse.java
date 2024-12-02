package sonnh.opt.opt_plan.payload.response;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryDetailResponse {
	private Long deliveryId;
	private String orderCode;
	private DeliveryStatus status;
	private CustomerInfo customer;
	private LocationInfo pickupLocation;
	private LocationInfo deliveryLocation;
	private Double totalWeight;
	private String deliveryNote;
	private LocalDateTime estimatedDeliveryTime;

	@Data
	@Builder
	public static class CustomerInfo {
		private Long customerId;
		private String name;
		private String phone;
		private String address;
	}

	@Data
	@Builder
	public static class LocationInfo {
		private Long locationId;
		private String address;
		private Double latitude;
		private Double longitude;
	}
}