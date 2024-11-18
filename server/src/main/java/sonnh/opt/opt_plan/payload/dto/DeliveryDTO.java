package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.*;
import sonnh.opt.opt_plan.model.Delivery;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
	private Long id;
	private DeliveryStatus status;
	private LocalDateTime deliveryDate;
	private OrderDTO order;
	private DriverDTO driver;
	private String currentLocation;
	private String deliveryNotes;

	public static DeliveryDTO fromEntity(Delivery delivery) {
		if (delivery == null)
			return null;
		return DeliveryDTO.builder().id(delivery.getId()).status(delivery.getStatus())
				.deliveryDate(delivery.getDeliveryDate())
				.order(OrderDTO.fromEntity(delivery.getOrder()))
				.driver(DriverDTO.fromEntity(delivery.getDriver()))
				.currentLocation(delivery.getOrder().getPickupWarehouse().getAddress())
				.deliveryNotes(delivery.getDeliveryNote()).build();
	}
}