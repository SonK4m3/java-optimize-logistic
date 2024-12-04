package sonnh.opt.opt_plan.payload.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.TaskStatus;
import sonnh.opt.opt_plan.payload.dto.StaffDTO;
import sonnh.opt.opt_plan.payload.dto.TaskDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentResponse {
	private Long id;
	private TaskDTO task;
	private StaffDTO staff;
	private TaskStatus status;
	private String note;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
