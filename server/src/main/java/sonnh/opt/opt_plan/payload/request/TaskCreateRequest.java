package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.TaskPriority;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {

	@NotNull(message = "Title cannot be null")
	@Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
	private String title;

	private String description;

	@NotNull(message = "Start time cannot be null")
	private LocalDateTime startTime;

	@NotNull(message = "End time cannot be null")
	private LocalDateTime endTime;

	@NotNull(message = "Priority cannot be null")
	private TaskPriority priority;
}
