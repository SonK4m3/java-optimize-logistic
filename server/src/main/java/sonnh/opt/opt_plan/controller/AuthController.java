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

import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.request.LoginRequest;
import sonnh.opt.opt_plan.payload.request.SignupRequest;
import sonnh.opt.opt_plan.payload.response.JwtResponse;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.utils.JwtUtil;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.constant.enums.Position;
import sonnh.opt.opt_plan.constant.enums.UserRole;
import sonnh.opt.opt_plan.exception.AuthenticationException;
import sonnh.opt.opt_plan.service.StaffService;
import sonnh.opt.opt_plan.service.CustomerService;

@RestController
@RequestMapping(Api.AUTH_ROUTE)
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {
    private final StaffService staffService;
    private final CustomerService customerService;
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
    public ResponseEntity<ApiResponse<String>> registerUser(
            @RequestBody SignupRequest signUpRequest) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email is already in use"));
        }

        // Create and save new user
        User user = User.builder().username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .fullName(signUpRequest.getFullName()).role(signUpRequest.getRole())
                .isActive(true).build();

        userRepository.save(user);

        if (signUpRequest.getRole() == UserRole.STAFF) {
            staffService.createStaff(Staff.builder().user(user).department(null)
                    .position(Position.WORKER).build());
        } else if (signUpRequest.getRole() == UserRole.CUSTOMER) {
            customerService.createCustomer(
                    Customer.builder().user(user).phone("0000000000").build());
        }

        return ResponseEntity.ok(ApiResponse.success("User registered successfully"));

    }
}
