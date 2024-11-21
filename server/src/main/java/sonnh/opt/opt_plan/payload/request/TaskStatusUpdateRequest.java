package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateRequest {
	@NotNull(message = "Status is required")
	private TaskStatus status;

	private String note;
}