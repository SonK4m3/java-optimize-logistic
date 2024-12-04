package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class VRPTaskDTO {
	private String taskId;
	private TaskStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private Double progress;
	private String errorMessage;
}