package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
	@NotBlank(message = "Role name is required")
	@Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
	private String name;

	@Size(max = 255, message = "Description cannot exceed 255 characters")
	private String description;

	@NotEmpty(message = "At least one permission is required")
	private Set<Long> permissionIds;
}
