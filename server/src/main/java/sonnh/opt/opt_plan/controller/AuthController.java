package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.request.LoginRequest;
import sonnh.opt.opt_plan.payload.request.SignupRequest;
import sonnh.opt.opt_plan.payload.response.JwtResponse;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.utils.JwtUtil;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.exception.AuthenticationException;

@RestController
@RequestMapping(Api.AUTH_ROUTE)
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;

    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new AuthenticationException(
                            "User not found with username: "
                                    + loginRequest.getUsername()));
            JwtResponse jwtResponse = new JwtResponse(jwt, user.getId(),
                    user.getUsername(), user.getEmail(), user.getRole(),
                    jwtUtils.getJwtExpirationMs());

            return ResponseEntity.ok(ApiResponse.success(jwtResponse));
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    @Operation(summary = "Register user", description = "Registers a new user in the system")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(
            @RequestBody SignupRequest signUpRequest) {
        try {
            // Validate email uniqueness
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Email is already in use"));
            }

            // Create and save new user
            User user = User.builder().username(signUpRequest.getUsername())
                    .email(signUpRequest.getEmail())
                    .password(encoder.encode(signUpRequest.getPassword()))
                    .fullName(signUpRequest.getFullName()).phone(signUpRequest.getPhone())
                    .address(signUpRequest.getAddress()).avatar(signUpRequest.getAvatar())
                    .bio(signUpRequest.getBio()).role(signUpRequest.getRole())
                    .isActive(true).build();

            userRepository.save(user);

            return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,
                    "Registration failed: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Logout user", description = "Invalidates user session and clears security context")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
}
