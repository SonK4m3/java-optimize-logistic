package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.UserRole;

@Data
@NoArgsConstructor
public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 100)
	private String fullName;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	private UserRole role = UserRole.CUSTOMER;
}
