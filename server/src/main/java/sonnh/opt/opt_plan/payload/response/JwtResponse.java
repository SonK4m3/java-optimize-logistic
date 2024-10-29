package sonnh.opt.opt_plan.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing JWT token and user details")
public class JwtResponse {
	@Schema(description = "JWT access token")
	private String token;

	@Schema(description = "Token type (Bearer)")
	private String type = "Bearer";

	@Schema(description = "User ID")
	private Long id;

	@Schema(description = "Username")
	private String username;

	@Schema(description = "Email address")
	private String email;

	@Schema(description = "List of user roles")
	private List<String> roles;

	@Schema(description = "Token expiration time in milliseconds")
	private Long expiresIn;

	// Constructor for basic token response
	public JwtResponse(String token) {
		this.token = token;
	}

	// Constructor for full user details
	public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	// Constructor with expiration
	public JwtResponse(String token, Long id, String username, String email, List<String> roles, Long expiresIn) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.expiresIn = expiresIn;
	}
}
