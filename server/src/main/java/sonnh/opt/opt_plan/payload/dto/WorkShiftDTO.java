package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.WorkShift;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkShiftDTO {
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private Boolean isActive;
	private List<ShiftAssignmentDTO> shiftAssignments;

	/**
	 * Convert WorkShift entity to WorkShiftDTO
	 * 
	 * @param workShift WorkShift entity to convert
	 * @return WorkShiftDTO object with mapped fields
	 */
	public static WorkShiftDTO fromEntity(WorkShift workShift) {
		if (workShift == null) {
			return null;
		}

		return WorkShiftDTO.builder().id(workShift.getId()).name(workShift.getName())
				.startTime(workShift.getStartTime()).endTime(workShift.getEndTime())
				.isActive(workShift.getIsActive())
				.shiftAssignments(workShift.getShiftAssignments() != null ? workShift
						.getShiftAssignments().stream()
						.map(ShiftAssignmentDTO::fromEntity).collect(Collectors.toList())
						: null)
				.build();
	}

}
