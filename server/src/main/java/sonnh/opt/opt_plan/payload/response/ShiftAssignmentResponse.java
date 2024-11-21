package sonnh.opt.opt_plan.payload.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.constant.enums.ShiftStatus;
import sonnh.opt.opt_plan.payload.dto.SimpleWorkShiftDTO;
import sonnh.opt.opt_plan.payload.dto.SimpleStaffDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignmentResponse {
	private Long id;
	private SimpleWorkShiftDTO workShift;
	private SimpleStaffDTO staff;
	private LocalDate workDate;
	private ShiftStatus status;
	private String note;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
