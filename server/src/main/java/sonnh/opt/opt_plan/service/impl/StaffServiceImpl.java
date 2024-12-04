package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.repository.StaffRepository;
import sonnh.opt.opt_plan.service.StaffService;
import sonnh.opt.opt_plan.constant.enums.Position;
import sonnh.opt.opt_plan.payload.request.StaffUpdateRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {
	private final StaffRepository staffRepository;

	@Override
	public Staff createStaff(Staff staff) { return staffRepository.save(staff); }

	@Override
	public Staff updateStaff(Long userId, Staff staff) {
		return staffRepository.findByUser_Id(userId).map(existingStaff -> {
			existingStaff.setPosition(staff.getPosition());
			existingStaff.setDepartment(staff.getDepartment());
			return staffRepository.save(existingStaff);
		}).orElseThrow(
				() -> new RuntimeException("Staff not found with userId: " + userId));
	}

	@Override
	public void deleteStaff(Long userId) {
		staffRepository.findByUser_Id(userId).ifPresent(staffRepository::delete);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Staff> getStaffByUserId(Long userId) {
		return staffRepository.findByUser_Id(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Staff> getStaffByDepartment(Long departmentId) {
		return staffRepository.findByDepartmentId(departmentId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Staff> getStaffByPosition(Position position) {
		return staffRepository.findByPosition(position);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Staff> getStaffWithShiftAssignments(Long departmentId) {
		return staffRepository.findByDepartmentWithShiftAssignments(departmentId);
	}

	@Override
	public Staff updateStaffInfo(Long userId, StaffUpdateRequest staffUpdateRequest) {
		return staffRepository.findByUser_Id(userId).map(existingStaff -> {
			existingStaff.setPosition(staffUpdateRequest.getPosition());
			return staffRepository.save(existingStaff);
		}).orElseThrow(
				() -> new RuntimeException("Staff not found with userId: " + userId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Staff> getAllStaff() { return staffRepository.findAll(); }

	@Override
	@Transactional(readOnly = true)
	public Optional<Staff> getStaffById(Long id) { return staffRepository.findById(id); }
}