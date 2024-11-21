package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.ShiftStatus;
import sonnh.opt.opt_plan.model.ShiftAssignment;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignmentDTO {
	private Long id;
	private Long workShiftId;
	private String workShiftName;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long staffId;
	private String staffName;
	private LocalDate workDate;
	private ShiftStatus status;
	private String note;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * Convert ShiftAssignment entity to ShiftAssignmentDTO
	 * 
	 * @param shiftAssignment ShiftAssignment entity to convert
	 * @return ShiftAssignmentDTO object with mapped fields
	 * @throws IllegalArgumentException if shiftAssignment is null or has
	 *                                  invalid data
	 */
	public static ShiftAssignmentDTO fromEntity(ShiftAssignment shiftAssignment) {
		if (shiftAssignment == null) {
			throw new IllegalArgumentException("ShiftAssignment cannot be null");
		}

		// Validate required relationships
		if (shiftAssignment.getWorkShift() == null) {
			throw new IllegalArgumentException("WorkShift relationship cannot be null");
		}
		if (shiftAssignment.getStaff() == null) {
			throw new IllegalArgumentException("Staff relationship cannot be null");
		}

		// Build DTO with null checks on optional fields
		return ShiftAssignmentDTO.builder().id(shiftAssignment.getId())
				.workShiftId(shiftAssignment.getWorkShift().getId())
				.workShiftName(shiftAssignment.getWorkShift().getName())
				.startTime(shiftAssignment.getWorkShift().getStartTime())
				.endTime(shiftAssignment.getWorkShift().getEndTime())
				.staffId(shiftAssignment.getStaff().getId())
				.staffName(shiftAssignment.getStaff().getUser().getFullName())
				.workDate(shiftAssignment.getWorkDate())
				.status(shiftAssignment.getStatus() != null ? shiftAssignment.getStatus()
						: ShiftStatus.SCHEDULED)
				.note(shiftAssignment.getNote()).createdAt(shiftAssignment.getCreatedAt())
				.updatedAt(shiftAssignment.getUpdatedAt()).build();
	}
}
