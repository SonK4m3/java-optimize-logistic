package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.TaskAssignment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentDTO {
	private Long id;
	private TaskDTO task;
	private StaffDTO staff;
	private String note;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * Convert TaskAssignment entity to TaskAssignmentDTO
	 * 
	 * @param taskAssignment TaskAssignment entity to convert
	 * @return TaskAssignmentDTO object with mapped fields
	 */
	public static TaskAssignmentDTO fromEntity(TaskAssignment taskAssignment) {
		if (taskAssignment == null) {
			return null;
		}

		return TaskAssignmentDTO.builder().id(taskAssignment.getId())
				.task(TaskDTO.fromEntity(taskAssignment.getTask()))
				.staff(StaffDTO.fromEntity(taskAssignment.getStaff()))
				.note(taskAssignment.getNote()).createdAt(taskAssignment.getCreatedAt())
				.updatedAt(taskAssignment.getUpdatedAt()).build();
	}
}
