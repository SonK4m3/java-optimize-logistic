package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.constant.enums.DeliveryAssignmentStatus;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignmentDTO {
	private Long id;
	private Long deliveryId;
	private Long driverId;
	private List<Long> warehouseIds;
	private LocalDateTime assignedAt;
	private DeliveryAssignmentStatus status;
	private String rejectionReason;
	private LocalDateTime respondedAt;
	private LocalDateTime expiresAt;

	/**
	 * Convert DeliveryAssignment entity to DeliveryAssignmentDTO
	 * 
	 * @param assignment DeliveryAssignment entity to convert
	 * @return DeliveryAssignmentDTO object with mapped fields
	 */
	public static DeliveryAssignmentDTO fromEntity(DeliveryAssignment assignment) {
		if (assignment == null)
			return null;
		return DeliveryAssignmentDTO.builder().id(assignment.getId())
				.deliveryId(assignment.getDelivery().getId())
				.driverId(assignment.getDriver().getId())
				.warehouseIds(assignment.getWarehouseIds())
				.assignedAt(assignment.getAssignedAt()).status(assignment.getStatus())
				.rejectionReason(assignment.getRejectionReason())
				.respondedAt(assignment.getRespondedAt())
				.expiresAt(assignment.getExpiresAt()).build();
	}
}
