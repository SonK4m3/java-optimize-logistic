package sonnh.opt.opt_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sonnh.opt.opt_plan.constant.enums.TaskStatus;
import sonnh.opt.opt_plan.payload.request.TaskAssignmentRequest;
import sonnh.opt.opt_plan.payload.request.TaskCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.payload.response.TaskAssignmentResponse;
import sonnh.opt.opt_plan.service.TaskAssignmentService;
import sonnh.opt.opt_plan.payload.dto.TaskDTO;
import sonnh.opt.opt_plan.payload.ApiResponse;

import java.time.LocalDate;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/task-assignments")
@RequiredArgsConstructor
public class TaskAssignmentController {
	private final TaskAssignmentService taskAssignmentService;

	/**
	 * Create new task Input: TaskRequest(name, description, startDate, endDate,
	 * status) Output: TaskResponse
	 */
	@PostMapping("/tasks")
	public ResponseEntity<ApiResponse<TaskDTO>> createTask(
			@RequestBody @Valid TaskCreateRequest request) {
		return ResponseEntity.ok(ApiResponse.success("Task created successfully",
				TaskDTO.fromEntity(taskAssignmentService.createTask(request))));
	}

	@GetMapping("/tasks")
	public ResponseEntity<ApiResponse<PageResponse<TaskDTO>>> getTasks(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDTO> tasks = taskAssignmentService
				.getTasks(PageRequest.of(page - 1, size));

		PageResponse<TaskDTO> response = PageResponse.<TaskDTO> builder()
				.docs(tasks.getContent()).totalDocs(tasks.getTotalElements()).limit(size)
				.page(page).totalPages(tasks.getTotalPages()).hasNextPage(tasks.hasNext())
				.hasPrevPage(tasks.hasPrevious()).build();

		return ResponseEntity
				.ok(ApiResponse.success("Tasks fetched successfully", response));
	}

	/**
	 * Create new task assignment Input: TaskAssignmentRequest(taskId, staffId,
	 * note) Output: TaskAssignmentResponse
	 */
	@PostMapping("/assignments")
	public ResponseEntity<TaskAssignmentResponse> createTaskAssignment(
			@RequestBody @Valid TaskAssignmentRequest request) {
		return ResponseEntity.ok(taskAssignmentService.createTaskAssignment(request));
	}

	/**
	 * Get task assignments with filters Input: staffId, taskId, startDate,
	 * endDate, status Output: List<TaskAssignmentResponse>
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<TaskAssignmentResponse>>> getTaskAssignments(
			@RequestParam(required = false) Long staffId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskAssignmentResponse> taskAssignments = taskAssignmentService
				.getTaskAssignments(staffId, PageRequest.of(page - 1, size));

		PageResponse<TaskAssignmentResponse> response = PageResponse
				.<TaskAssignmentResponse> builder().docs(taskAssignments.getContent())
				.totalDocs(taskAssignments.getTotalElements()).limit(size).page(page)
				.totalPages(taskAssignments.getTotalPages())
				.hasNextPage(taskAssignments.hasNext())
				.hasPrevPage(taskAssignments.hasPrevious()).build();

		return ResponseEntity.ok(
				ApiResponse.success("Task assignments fetched successfully", response));
	}
}