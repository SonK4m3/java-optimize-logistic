package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.ShiftStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftStatusUpdateRequest {
	@NotNull(message = "Status is required")
	private ShiftStatus status;

	private String note;

	@Builder.Default
	private Boolean notifyStaff = false;
}