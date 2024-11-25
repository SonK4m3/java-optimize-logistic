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
	private OrderDTO order;
	private DeliveryStatus status;
	private String deliveryNote;
	private LocalDateTime deliveryDate;
	private DriverDTO driver;

	public static DeliveryDTO fromEntity(Delivery delivery) {
		if (delivery == null)
			return null;
		return DeliveryDTO.builder().id(delivery.getId()).status(delivery.getStatus())
				.deliveryDate(delivery.getDeliveryDate())
				.order(OrderDTO.fromEntity(delivery.getOrder()))
				.driver(DriverDTO.fromEntity(delivery.getDriver()))
				.deliveryNote(delivery.getDeliveryNote()).build();
	}
}