package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.model.DeliveryStop;
import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryStopDTO {
	private Long id;
	private Long routeId;
	private Long deliveryId;
	private Integer sequenceNumber;
	private LocalDateTime plannedArrival;
	private String status;

	public static DeliveryStopDTO fromEntity(DeliveryStop stop) {
		if (stop == null)
			return null;

		return DeliveryStopDTO.builder().id(stop.getId())
				.routeId(stop.getRoute() != null ? stop.getRoute().getId() : null)
				.deliveryId(
						stop.getDelivery() != null ? stop.getDelivery().getId() : null)
				.sequenceNumber(stop.getSequenceNumber())
				.plannedArrival(stop.getPlannedArrival())
				.status(stop.getStatus().toString()).build();
	}
}