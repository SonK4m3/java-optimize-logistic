package sonnh.opt.opt_plan.payload.request;

import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

@Data
public class DeliveryActionRequest {
	private DeliveryStatus newStatus;
	private String note;
	private Double latitude;
	private Double longitude;
	private String signature; // Customer signature for proof of delivery
	private String photoUrl; // Photo proof of delivery
}