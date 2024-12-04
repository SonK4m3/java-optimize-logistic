package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

@Data
public class DeliveryUpdateRequest {
	@NotNull
	private DeliveryStatus status;

	private String note;

	@NotNull
	private String location;

	private Long driverId;
}
