package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentRequest {
	@NotNull
	private Long taskId;

	@NotNull
	private Long staffId;

	private String note;
}
