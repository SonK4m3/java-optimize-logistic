package sonnh.opt.opt_plan.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sonnh.opt.opt_plan.model.ShiftAssignment;
import sonnh.opt.opt_plan.payload.response.ShiftAssignmentResponse;
import sonnh.opt.opt_plan.payload.dto.SimpleWorkShiftDTO;
import sonnh.opt.opt_plan.payload.dto.SimpleStaffDTO;
import sonnh.opt.opt_plan.model.WorkShift;
import sonnh.opt.opt_plan.model.Staff;

@Component
@RequiredArgsConstructor
public class ShiftAssignmentMapper {

	public ShiftAssignmentResponse toResponse(ShiftAssignment assignment) {
		return ShiftAssignmentResponse.builder().id(assignment.getId())
				.workShift(toSimpleWorkShiftDTO(assignment.getWorkShift()))
				.staff(toSimpleStaffDTO(assignment.getStaff()))
				.workDate(assignment.getWorkDate()).status(assignment.getStatus())
				.note(assignment.getNote()).createdAt(assignment.getCreatedAt())
				.updatedAt(assignment.getUpdatedAt()).build();
	}

	private SimpleWorkShiftDTO toSimpleWorkShiftDTO(WorkShift workShift) {
		if (workShift == null)
			return null;
		return SimpleWorkShiftDTO.builder().id(workShift.getId())
				.name(workShift.getName()).startTime(workShift.getStartTime())
				.endTime(workShift.getEndTime()).build();
	}

	private SimpleStaffDTO toSimpleStaffDTO(Staff staff) {
		if (staff == null)
			return null;
		return SimpleStaffDTO.builder().id(staff.getId())
				.fullName(staff.getUser() != null ? staff.getUser().getFullName() : null)
				.position(staff.getPosition().name()).build();
	}
}