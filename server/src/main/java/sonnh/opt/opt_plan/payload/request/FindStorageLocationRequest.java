package sonnh.opt.opt_plan.payload.request;

import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.StorageLocationType;

@Data
public class FindStorageLocationRequest {
	private Long warehouseId;
	private Double requiredWeight;
	private StorageLocationType preferredType;
	private Double minHeight;
	private Double minWidth;
	private Double minLength;
}