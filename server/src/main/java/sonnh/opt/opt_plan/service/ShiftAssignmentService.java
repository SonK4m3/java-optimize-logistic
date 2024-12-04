package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.ShiftStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.mapper.ShiftAssignmentMapper;
import sonnh.opt.opt_plan.model.ShiftAssignment;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.model.WorkShift;
import sonnh.opt.opt_plan.payload.dto.WorkShiftDTO;
import sonnh.opt.opt_plan.payload.request.BatchShiftAssignmentRequest;
import sonnh.opt.opt_plan.payload.request.ShiftCreateRequest;
import sonnh.opt.opt_plan.payload.request.ShiftStatusUpdateRequest;
import sonnh.opt.opt_plan.payload.response.ShiftAssignmentResponse;
import sonnh.opt.opt_plan.repository.ShiftAssignmentRepository;
import sonnh.opt.opt_plan.repository.StaffRepository;
import sonnh.opt.opt_plan.repository.WorkShiftRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShiftAssignmentService {
	private final ShiftAssignmentRepository shiftAssignmentRepository;
	private final WorkShiftRepository workShiftRepository;
	private final StaffRepository staffRepository;
	private final ShiftAssignmentMapper mapper;

	/**
	 * Create shift assignments for multiple staff members Input: -
	 * BatchShiftAssignmentRequest containing: - shiftId: ID of the work shift -
	 * staffIds: List of staff member IDs - workDate: Date for the shift
	 * assignment - note: Optional note for the assignments Output: -
	 * List<ShiftAssignmentResponse> containing created assignments
	 * 
	 * @throws ResourceNotFoundException if work shift or staff not found
	 * @throws IllegalStateException     if assignments already exist
	 */
	@Transactional
	public List<ShiftAssignmentResponse> createShiftAssignments(
			BatchShiftAssignmentRequest request) {
		try {
			WorkShift workShift = workShiftRepository.findById(request.getShiftId())
					.orElseThrow(
							() -> new ResourceNotFoundException("WorkShift not found"));

			List<Staff> staffList = staffRepository.findAllById(request.getStaffIds());
			if (staffList.size() != request.getStaffIds().size()) {
				throw new ResourceNotFoundException("Some staff members not found");
			}

			LocalDate workDate = LocalDate.parse(request.getWorkDate());

			// Check existing assignments
			List<ShiftAssignment> existingAssignments = shiftAssignmentRepository
					.findByWorkShiftIdAndWorkDateAndStaffIdIn(workShift.getId(), workDate,
							request.getStaffIds());

			if (!existingAssignments.isEmpty()) {
				// Filter out staff IDs that already have assignments
				List<Long> existingStaffIds = existingAssignments.stream()
						.map(sa -> sa.getStaff().getId()).collect(Collectors.toList());

				// Remove existing staff from the request list
				request.getStaffIds().removeAll(existingStaffIds);

				// If all staff already assigned, return empty list
				if (request.getStaffIds().isEmpty()) {
					throw new IllegalStateException(
							"All staff members are already assigned to this shift on the given date");
				}
			}

			List<ShiftAssignment> assignments = staffList.stream()
					.map(staff -> ShiftAssignment.builder().workShift(workShift)
							.staff(staff).workDate(workDate).status(ShiftStatus.SCHEDULED)
							.note(request.getNote()).build())
					.collect(Collectors.toList());

			List<ShiftAssignment> savedAssignments = shiftAssignmentRepository
					.saveAll(assignments);

			return savedAssignments.stream().map(mapper::toResponse)
					.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("Error creating shift assignments: ", e);
			throw new RuntimeException(
					"Failed to create shift assignments: " + e.getMessage());
		}
	}

	/**
	 * Get shift assignments with filters
	 * 
	 * @param staffId   Optional staff filter
	 * @param shiftId   Optional shift filter
	 * @param startDate Optional start date filter
	 * @param endDate   Optional end date filter
	 * @param status    Optional status filter
	 * @param pageable  Pagination information
	 * @return Page<ShiftAssignmentResponse>
	 */
	public Page<ShiftAssignmentResponse> getShiftAssignments(Long staffId, Long shiftId,
			Pageable pageable) {

		if (staffId != null) {
			return shiftAssignmentRepository.findByStaffId(staffId, pageable)
					.map(mapper::toResponse);
		}

		if (shiftId != null) {
			return shiftAssignmentRepository.findByWorkShiftId(shiftId, pageable)
					.map(mapper::toResponse);
		}

		return shiftAssignmentRepository.findAll(pageable).map(mapper::toResponse);
	}

	/**
	 * Update shift assignment status
	 * 
	 * @param id      Shift assignment ID
	 * @param request Contains new status and note
	 * @return Updated ShiftAssignmentResponse
	 * @throws ResourceNotFoundException if shift assignment not found
	 */
	public ShiftAssignmentResponse updateShiftStatus(Long id,
			ShiftStatusUpdateRequest request) {
		ShiftAssignment shiftAssignment = shiftAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Shift assignment not found"));

		shiftAssignment.setStatus(request.getStatus());
		shiftAssignment.setNote(request.getNote());

		return mapper.toResponse(shiftAssignmentRepository.save(shiftAssignment));
	}

	/**
	 * Create new work shift
	 * 
	 * @param request
	 * @return
	 */
	public WorkShift createWorkShift(ShiftCreateRequest request) {
		WorkShift workShift = WorkShift.builder().name(request.getName())
				.startTime(request.getStartTime()).endTime(request.getEndTime())
				.isActive(true).build();

		return workShiftRepository.save(workShift);
	}

	public Page<WorkShiftDTO> getWorkShifts(Pageable pageable) {
		return workShiftRepository.findAll(pageable).map(WorkShiftDTO::fromEntity);
	}
}