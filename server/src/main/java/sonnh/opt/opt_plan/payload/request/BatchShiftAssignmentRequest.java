package sonnh.opt.opt_plan.payload.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchShiftAssignmentRequest {
	@NotNull
	private Long shiftId;

	@NotEmpty
	private List<Long> staffIds;

	@NotNull
	private String workDate;

	private String note;
}
