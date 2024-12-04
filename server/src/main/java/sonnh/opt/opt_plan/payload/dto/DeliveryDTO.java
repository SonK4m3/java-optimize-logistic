package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.Delivery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
	private OrderWithFee order;
	private DeliveryStatus status;
	private String deliveryNote;
	private LocationDTO deliveryLocation;
	private Double estimatedDistance;
	private LocalDateTime estimatedDeliveryTime;
	private LocalDateTime actualDeliveryTime;
	private DriverDTO driver;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<Long> warehouseList;
	private List<DeliveryStatusHistoryDTO> statusHistory;

	/**
	 * Maps a Delivery entity to a DeliveryDTO.
	 * 
	 * @param delivery the Delivery entity to map.
	 * @return the mapped DeliveryDTO.
	 */
	public static DeliveryDTO fromEntity(Delivery delivery) {
		if (delivery == null)
			return null;

		delivery.getOrder().setDelivery(null);

		return DeliveryDTO.builder().id(delivery.getId()).status(delivery.getStatus())
				.estimatedDistance(delivery.getEstimatedDistance())
				.estimatedDeliveryTime(delivery.getEstimatedDeliveryTime())
				.actualDeliveryTime(delivery.getActualDeliveryTime())
				.order(OrderWithFee.fromEntity(delivery.getOrder()))
				.driver(DriverDTO.fromEntity(delivery.getDriver()))
				.deliveryLocation(LocationDTO.fromEntity(delivery.getDeliveryLocation()))
				.deliveryNote(delivery.getDeliveryNote())
				.warehouseList(delivery.getWarehouseList())
				.statusHistory(delivery.getStatusHistory().stream()
						.map(DeliveryStatusHistoryDTO::fromEntity)
						.collect(Collectors.toList()))
				.createdAt(delivery.getCreatedAt()).updatedAt(delivery.getUpdatedAt())
				.build();
	}
}