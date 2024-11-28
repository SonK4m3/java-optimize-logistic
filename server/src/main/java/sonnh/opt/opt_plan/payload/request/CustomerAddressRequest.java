package sonnh.opt.opt_plan.payload.request;

import lombok.Data;

@Data
public class CustomerAddressRequest {
	private String address;
	private Double latitude;
	private Double longitude;
	private Boolean isDefault;
}
