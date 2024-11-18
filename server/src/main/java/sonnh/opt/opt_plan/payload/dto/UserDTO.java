package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long id;
	private String username;
	private String fullName;
	private String email;
	private String phone;

	public static UserDTO fromEntity(User user) {
		if (user == null)
			return null;
		return UserDTO.builder().id(user.getId()).username(user.getUsername())
				.fullName(user.getFullName()).email(user.getEmail())
				.phone(user.getPhone()).build();
	}
}