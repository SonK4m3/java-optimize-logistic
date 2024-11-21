package sonnh.opt.opt_plan.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sonnh.opt.opt_plan.model.TaskAssignment;
import sonnh.opt.opt_plan.payload.response.TaskAssignmentResponse;
import sonnh.opt.opt_plan.payload.dto.TaskDTO;
import sonnh.opt.opt_plan.payload.dto.StaffDTO;

@Component
@RequiredArgsConstructor
public class TaskAssignmentMapper {

	public TaskAssignmentResponse toResponse(TaskAssignment taskAssignment) {
		return TaskAssignmentResponse.builder().id(taskAssignment.getId())
				.task(TaskDTO.fromEntity(taskAssignment.getTask()))
				.staff(StaffDTO.fromEntity(taskAssignment.getStaff()))
				.note(taskAssignment.getNote()).createdAt(taskAssignment.getCreatedAt())
				.updatedAt(taskAssignment.getUpdatedAt()).build();
	}
}