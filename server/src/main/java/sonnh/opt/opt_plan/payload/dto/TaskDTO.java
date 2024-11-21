package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.TaskPriority;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;
import sonnh.opt.opt_plan.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
	private Long id;
	private String title;
	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private TaskPriority priority;
	private TaskStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * Converts a Task entity to a TaskDTO.
	 *
	 * @param task the Task entity to convert
	 * @return a TaskDTO object with mapped fields, or null if the input task is
	 *         null
	 */
	public static TaskDTO fromEntity(Task task) {
		if (task == null) {
			return null;
		}

		return TaskDTO.builder().id(task.getId()).title(task.getTitle())
				.description(task.getDescription()).startTime(task.getStartTime())
				.endTime(task.getEndTime()).priority(task.getPriority())
				.status(task.getStatus()).createdAt(task.getCreatedAt())
				.updatedAt(task.getUpdatedAt()).build();
	}
}
