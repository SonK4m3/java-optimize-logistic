package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.CustomerAddress;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressDTO {
	private Long id;
	private LocationDTO location;
	private Boolean isDefault;

	public static CustomerAddressDTO fromEntity(CustomerAddress customerAddress) {
		if (customerAddress == null) {
			return null;
		}

		return CustomerAddressDTO.builder().id(customerAddress.getId())
				.location(LocationDTO.fromEntity(customerAddress.getLocation()))
				.isDefault(customerAddress.getIsDefault()).build();
	}
}
