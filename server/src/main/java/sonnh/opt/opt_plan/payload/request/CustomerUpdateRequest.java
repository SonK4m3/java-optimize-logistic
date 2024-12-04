package sonnh.opt.opt_plan.payload.request;

import lombok.Data;

@Data
public class CustomerUpdateRequest {
	private String phone;
	private CustomerAddressRequest address;
}
