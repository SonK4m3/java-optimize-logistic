package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleStaffDTO {
	private Long id;
	private String fullName;
	private String position;
}
