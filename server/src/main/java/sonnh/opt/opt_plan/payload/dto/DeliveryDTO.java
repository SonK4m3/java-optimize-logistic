package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.Delivery;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Delivery entity.
 * 
 * @author Son Nguyen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
	private Long id;
	private OrderDTO order;
	private DeliveryStatus status;
	private String deliveryNote;
	private LocationDTO pickupLocation;
	private Double estimatedDistance;
	private LocalDateTime estimatedDeliveryTime;
	private LocalDateTime actualDeliveryTime;
	private DriverDTO driver;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * Maps a Delivery entity to a DeliveryDTO.
	 * 
	 * @param delivery the Delivery entity to map.
	 * @return the mapped DeliveryDTO.
	 */
	public static DeliveryDTO fromEntity(Delivery delivery) {
		if (delivery == null)
			return null;
		return DeliveryDTO.builder().id(delivery.getId()).status(delivery.getStatus())
				.estimatedDistance(delivery.getEstimatedDistance())
				.estimatedDeliveryTime(delivery.getEstimatedDeliveryTime())
				.actualDeliveryTime(delivery.getActualDeliveryTime())
				.order(OrderDTO.fromEntity(delivery.getOrder()))
				.driver(DriverDTO.fromEntity(delivery.getDriver()))
				.pickupLocation(LocationDTO.fromEntity(delivery.getPickupLocation()))
				.deliveryNote(delivery.getDeliveryNote())
				.createdAt(delivery.getCreatedAt()).updatedAt(delivery.getUpdatedAt())
				.build();
	}
}