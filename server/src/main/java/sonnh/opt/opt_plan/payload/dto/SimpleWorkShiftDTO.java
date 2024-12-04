package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleWorkShiftDTO {
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
}
