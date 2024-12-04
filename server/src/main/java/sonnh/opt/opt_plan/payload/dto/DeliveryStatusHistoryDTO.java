package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.DeliveryStatusHistory;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for DeliveryStatusHistory entity.
 * 
 * @author Son Nguyen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusHistoryDTO {
	private DeliveryStatus status;
	private LocalDateTime timestamp;
	private String note;
	private LocationDTO location;
	private String updatedBy;

	/**
	 * Maps a DeliveryStatusHistory entity to a DeliveryStatusHistoryDTO.
	 * 
	 * @param history the DeliveryStatusHistory entity to map.
	 * @return the mapped DeliveryStatusHistoryDTO.
	 */
	public static DeliveryStatusHistoryDTO fromEntity(DeliveryStatusHistory history) {
		if (history == null)
			return null;

		return DeliveryStatusHistoryDTO.builder().status(history.getStatus())
				.timestamp(history.getTimestamp()).note(history.getNote())
				.location(LocationDTO.fromEntity(history.getLocation()))
				.updatedBy(history.getUpdatedBy()).build();
	}
}
