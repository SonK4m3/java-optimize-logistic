
package sonnh.opt.opt_plan.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.UserRole;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing JWT token and user details")
public class JwtResponse {
	@Schema(description = "JWT access token")
	private String token;

	@Schema(description = "User ID")
	private Long id;

	@Schema(description = "Username")
	private String username;

	@Schema(description = "Email address")
	private String email;

	@Schema(description = "User role")
	private UserRole role;

	@Schema(description = "Token expiration time in milliseconds")
	private Long expiresIn;

	@Schema(description = "Token issued time")
	private Instant issuedAt;

	@Schema(description = "Token expiration time")
	private Instant expirationTime;

	@Schema(description = "Last login time")
	private Instant lastLoginTime;

	// Constructor for basic token response
	public JwtResponse(String token) {
		this.token = token;
		this.issuedAt = Instant.now();
		this.lastLoginTime = Instant.now();
	}

	// Constructor for full user details
	public JwtResponse(String token, Long id, String username, String email,
			UserRole role) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.issuedAt = Instant.now();
		this.lastLoginTime = Instant.now();
	}

	// Constructor with expiration
	public JwtResponse(String token, Long id, String username, String email,
			UserRole role, Long expiresIn) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.expiresIn = expiresIn;
		this.issuedAt = Instant.now();
		this.expirationTime = Instant.now().plusMillis(expiresIn);
		this.lastLoginTime = Instant.now();
	}
}
