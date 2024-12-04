package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.StorageLocationType;

@Data
public class CreateStorageLocationRequest {
	@NotNull
	private Long storageAreaId;

	@NotNull
	private StorageLocationType type;

	@Positive
	private Double length;

	@Positive
	private Double width;

	@Positive
	private Double height;

	@Positive
	private Double maxWeight;

	@NotNull
	private Integer level;

	@NotNull
	private Integer position;
}