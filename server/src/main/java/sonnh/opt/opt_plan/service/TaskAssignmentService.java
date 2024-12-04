package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.model.TaskAssignment;
import sonnh.opt.opt_plan.payload.dto.TaskDTO;
import sonnh.opt.opt_plan.payload.request.TaskAssignmentRequest;
import sonnh.opt.opt_plan.payload.request.TaskCreateRequest;
import sonnh.opt.opt_plan.payload.response.TaskAssignmentResponse;
import sonnh.opt.opt_plan.repository.StaffRepository;
import sonnh.opt.opt_plan.repository.TaskAssignmentRepository;
import sonnh.opt.opt_plan.repository.TaskRepository;
import sonnh.opt.opt_plan.model.Task;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;
import sonnh.opt.opt_plan.payload.request.TaskStatusUpdateRequest;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.mapper.TaskAssignmentMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskAssignmentService {
	private final TaskAssignmentRepository taskAssignmentRepository;
	private final TaskRepository taskRepository;
	private final StaffRepository staffRepository;
	private final TaskAssignmentMapper mapper;

	/**
	 * Create new task assignment
	 * 
	 * @param request Contains taskId, staffId, note
	 * @return TaskAssignmentResponse
	 * @throws ResourceNotFoundException if task or staff not found
	 */
	public TaskAssignmentResponse createTaskAssignment(TaskAssignmentRequest request) {
		Task task = taskRepository.findById(request.getTaskId())
				.orElseThrow(() -> new ResourceNotFoundException("Task not found"));

		Staff staff = staffRepository.findById(request.getStaffId())
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

		TaskAssignment taskAssignment = TaskAssignment.builder().task(task).staff(staff)
				.note(request.getNote()).build();

		return mapper.toResponse(taskAssignmentRepository.save(taskAssignment));
	}

	/**
	 * Get task assignments with filters
	 * 
	 * @param staffId   Optional staff filter
	 * @param taskId    Optional task filter
	 * @param startDate Optional start date filter
	 * @param endDate   Optional end date filter
	 * @param status    Optional status filter
	 * @param pageable  Pagination information
	 * @return Page<TaskAssignmentResponse>
	 */
	public Page<TaskAssignmentResponse> getTaskAssignments(Long staffId,
			Pageable pageable) {

		Specification<TaskAssignment> spec = Specification.where(null);

		if (staffId != null) {
			spec = spec.and(
					(root, query, cb) -> cb.equal(root.get("staff").get("id"), staffId));
		}

		return taskAssignmentRepository.findAll(spec, pageable).map(mapper::toResponse);
	}

	/**
	 * Update task assignment status
	 * 
	 * @param id      Task assignment ID
	 * @param request Contains new status and note
	 * @return Updated TaskAssignmentResponse
	 * @throws ResourceNotFoundException if task assignment not found
	 */
	public TaskAssignmentResponse updateTaskStatus(Long id,
			TaskStatusUpdateRequest request) {
		TaskAssignment taskAssignment = taskAssignmentRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Task assignment not found"));

		taskAssignment.getTask().setStatus(request.getStatus());
		taskAssignment.setNote(request.getNote());

		return mapper.toResponse(taskAssignmentRepository.save(taskAssignment));
	}

	/**
	 * Create new task
	 * 
	 * @param request TaskCreateRequest
	 * @return Task
	 */
	public Task createTask(TaskCreateRequest request) {
		Task task = Task.builder().title(request.getTitle())
				.description(request.getDescription()).startTime(request.getStartTime())
				.endTime(request.getEndTime()).priority(request.getPriority())
				.status(TaskStatus.CREATED).build();

		return taskRepository.save(task);
	}

	public Page<TaskDTO> getTasks(Pageable pageable) {
		return taskRepository.findAll(pageable).map(TaskDTO::fromEntity);
	}
}