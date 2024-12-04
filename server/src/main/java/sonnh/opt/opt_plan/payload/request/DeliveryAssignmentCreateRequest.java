package sonnh.opt.opt_plan.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignmentCreateRequest {
	private Long deliveryId;
	private Long driverId;
	private List<Long> warehouseIds;
}
