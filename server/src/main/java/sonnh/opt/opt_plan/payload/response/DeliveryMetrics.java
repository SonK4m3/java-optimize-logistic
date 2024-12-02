package sonnh.opt.opt_plan.payload.response;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import java.time.LocalDateTime;
import sonnh.opt.opt_plan.model.Delivery;

@Data
@Builder
public class DeliveryMetrics {
	private Long deliveryId;
	private DeliveryStatus status;
	private Double estimatedDistance;
	private LocalDateTime estimatedDeliveryTime;
	private LocalDateTime actualDeliveryTime;
	private String driverName;
	private Double driverRating;

	public static DeliveryMetrics fromEntity(Delivery delivery) {
		return DeliveryMetrics.builder().deliveryId(delivery.getId())
				.status(delivery.getStatus())
				.estimatedDistance(delivery.getEstimatedDistance())
				.estimatedDeliveryTime(delivery.getEstimatedDeliveryTime())
				.actualDeliveryTime(delivery.getActualDeliveryTime())
				.driverName(delivery.getDriver().getUser().getFullName())
				.driverRating(delivery.getDriver().getRating()).build();
	}
}