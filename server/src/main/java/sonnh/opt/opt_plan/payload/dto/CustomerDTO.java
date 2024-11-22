package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.Customer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	private Long id;
	private String username;
	private String fullName;
	private String email;
	private String phone;

	public static CustomerDTO fromEntity(Customer customer) {
		if (customer == null) {
			return null;
		}

		return CustomerDTO.builder().id(customer.getUser().getId())
				.username(customer.getUser().getUsername())
				.fullName(customer.getUser().getFullName())
				.email(customer.getUser().getEmail()).phone(customer.getPhone()).build();
	}
}