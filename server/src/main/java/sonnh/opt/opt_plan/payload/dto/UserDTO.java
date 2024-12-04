package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.constant.enums.UserRole;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long id;
	private String username;
	private String fullName;
	private String email;
	private boolean isActive;
	private UserRole role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	/**
	 * Convert User entity to UserDTO
	 * 
	 * @param user User entity to convert
	 * @return UserDTO object with mapped fields
	 */
	public static UserDTO fromEntity(User user) {
		if (user == null)
			return null;
		return UserDTO.builder().id(user.getId()).username(user.getUsername())
				.fullName(user.getFullName()).email(user.getEmail()).role(user.getRole())
				.isActive(user.isActive()).createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt()).build();
	}
}