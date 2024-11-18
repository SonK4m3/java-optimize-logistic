package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.service.UserService;
import sonnh.opt.opt_plan.constant.common.Api;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Api.USER_ROUTE)
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
	private final UserService userService;

	@Operation(summary = "Create a new user")
	@PostMapping
	public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
		User createdUser = userService.createUser(user);
		return ResponseEntity
				.ok(ApiResponse.success("User created successfully", createdUser));
	}

	@Operation(summary = "Update an existing user")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id,
			@Valid @RequestBody User user) {
		User updatedUser = userService.updateUser(id, user);
		return ResponseEntity
				.ok(ApiResponse.success("User updated successfully", updatedUser));
	}

	@Operation(summary = "Get user by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);
		return ResponseEntity.ok(ApiResponse.success(user));
	}

	@Operation(summary = "Get all users")
	@GetMapping
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(ApiResponse.success(users));
	}

	@Operation(summary = "Delete a user")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
	}
}