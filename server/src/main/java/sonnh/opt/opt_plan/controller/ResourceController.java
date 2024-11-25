package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.service.UserService;
import sonnh.opt.opt_plan.service.StaffService;
import sonnh.opt.opt_plan.service.CustomerService;
import sonnh.opt.opt_plan.constant.common.Api;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import sonnh.opt.opt_plan.payload.dto.CustomerDTO;
import sonnh.opt.opt_plan.payload.dto.StaffDTO;
import sonnh.opt.opt_plan.payload.request.StaffUpdateRequest;
import sonnh.opt.opt_plan.payload.request.CustomerUpdateRequest;

@RestController
@RequestMapping(Api.RESOURCE_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Resource Management", description = "APIs for managing resources")
public class ResourceController {
	private final StaffService staffService;
	private final CustomerService customerService;

	@PutMapping("/staff/{userId}/info")
	@Operation(summary = "Update Staff Information", description = "Updates the information of an existing staff member.")
	public ResponseEntity<ApiResponse<StaffDTO>> updateStaff(@PathVariable Long userId,
			@Valid @RequestBody StaffUpdateRequest staffUpdateRequest) {
		Staff updatedStaff = staffService.updateStaffInfo(userId, staffUpdateRequest);
		return ResponseEntity
				.ok(ApiResponse.success("Staff information updated successfully",
						StaffDTO.fromEntity(updatedStaff)));
	}

	@PutMapping("/customer/{userId}/info")
	@Operation(summary = "Update Customer Information", description = "Updates the information of an existing customer.")
	public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(
			@PathVariable Long userId,
			@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
		Customer updatedCustomer = customerService.updateCustomerInfo(userId,
				customerUpdateRequest);
		return ResponseEntity
				.ok(ApiResponse.success("Customer information updated successfully",
						CustomerDTO.fromEntity(updatedCustomer)));
	}

	@GetMapping("/customers")
	@Operation(summary = "Get List of Customers", description = "Retrieves a list of all customers.")
	public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(ApiResponse.success(customers.stream()
				.map(CustomerDTO::fromEntity).collect(Collectors.toList())));
	}

	@GetMapping("/staffs")
	@Operation(summary = "Get List of Staff", description = "Retrieves a list of all staff members.")
	public ResponseEntity<ApiResponse<List<StaffDTO>>> getAllStaff() {
		List<Staff> staffMembers = staffService.getAllStaff();
		return ResponseEntity.ok(ApiResponse.success(staffMembers.stream()
				.map(StaffDTO::fromEntity).collect(Collectors.toList())));
	}

	@GetMapping("/staff/{id}")
	@Operation(summary = "Get Staff by ID", description = "Retrieves a staff member by their ID.")
	public ResponseEntity<ApiResponse<StaffDTO>> getStaffById(@PathVariable Long id) {
		Staff staff = staffService.getStaffById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
		return ResponseEntity.ok(ApiResponse.success("Staff retrieved successfully",
				StaffDTO.fromEntity(staff)));
	}
}