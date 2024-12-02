package sonnh.opt.opt_plan.payload.request;

import java.util.List;

import lombok.Data;

@Data
public class SuggestedDriversRequest {
	private Long deliveryId;
	private Long driverNumber;
	private List<Long> driverIds;
}
