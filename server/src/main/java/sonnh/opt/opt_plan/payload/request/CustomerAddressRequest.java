package sonnh.opt.opt_plan.payload.request;

import lombok.Data;

@Data
public class CustomerAddressRequest {
	private String address;
	private String city;
	private String country;
	private Boolean isDefault;
	private String addressType;
	private String recipientInfo;
}
