package sonnh.opt.opt_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import sonnh.opt.opt_plan.service.ShiftAssignmentService;
import sonnh.opt.opt_plan.payload.request.BatchShiftAssignmentRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.payload.response.ShiftAssignmentResponse;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.WorkShiftDTO;
import sonnh.opt.opt_plan.payload.request.ShiftCreateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shift-assignments")
@RequiredArgsConstructor
public class ShiftAssignmentController {
	private final ShiftAssignmentService shiftAssignmentService;

	/**
	 * Create new shift Input: ShiftRequest(name, description, startTime,
	 * endTime) Output: ShiftResponse
	 */
	@PostMapping("/shifts")
	public ResponseEntity<ApiResponse<WorkShiftDTO>> createWorkShift(
			@RequestBody @Valid ShiftCreateRequest request) {
		return ResponseEntity
				.ok(ApiResponse.success("Work shift created successfully", WorkShiftDTO
						.fromEntity(shiftAssignmentService.createWorkShift(request))));
	}

	@GetMapping("/shifts")
	public ResponseEntity<ApiResponse<PageResponse<WorkShiftDTO>>> getWorkShifts(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<WorkShiftDTO> workShifts = shiftAssignmentService
				.getWorkShifts(PageRequest.of(page - 1, size));

		PageResponse<WorkShiftDTO> response = PageResponse.<WorkShiftDTO> builder()
				.docs(workShifts.getContent()).totalDocs(workShifts.getTotalElements())
				.limit(size).page(page).totalPages(workShifts.getTotalPages())
				.hasNextPage(workShifts.hasNext()).hasPrevPage(workShifts.hasPrevious())
				.build();

		return ResponseEntity
				.ok(ApiResponse.success("Work shifts fetched successfully", response));
	}

	/**
	 * Create shift assignments for multiple staff Input:
	 * List<ShiftAssignmentRequest>(shiftId, staffIds, workDate) Output:
	 * List<ShiftAssignmentResponse>
	 */
	@PostMapping("/batch")
	public ResponseEntity<ApiResponse<List<ShiftAssignmentResponse>>> createShiftAssignments(
			@RequestBody @Valid BatchShiftAssignmentRequest request) {
		return ResponseEntity
				.ok(ApiResponse.success("Shift assignments created successfully",
						shiftAssignmentService.createShiftAssignments(request)));
	}

	/**
	 * Get shift assignments with filters Input: staffId, shiftId, startDate,
	 * endDate, status Output: Page<ShiftAssignmentResponse>
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<ShiftAssignmentResponse>>> getShiftAssignments(
			@RequestParam(required = false) Long staffId,
			@RequestParam(required = false) Long shiftId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<ShiftAssignmentResponse> shiftAssignments = shiftAssignmentService
				.getShiftAssignments(staffId, shiftId, PageRequest.of(page - 1, size));

		PageResponse<ShiftAssignmentResponse> response = PageResponse
				.<ShiftAssignmentResponse> builder().docs(shiftAssignments.getContent())
				.totalDocs(shiftAssignments.getTotalElements()).limit(size).page(page)
				.totalPages(shiftAssignments.getTotalPages())
				.hasNextPage(shiftAssignments.hasNext())
				.hasPrevPage(shiftAssignments.hasPrevious()).build();

		return ResponseEntity.ok(
				ApiResponse.success("Shift assignments fetched successfully", response));
	}
}