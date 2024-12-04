package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.payload.request.StaffUpdateRequest;
import sonnh.opt.opt_plan.constant.enums.Position;

import java.util.List;
import java.util.Optional;

public interface StaffService {
	Staff createStaff(Staff staff);

	Staff updateStaff(Long userId, Staff staff);

	void deleteStaff(Long userId);

	Optional<Staff> getStaffByUserId(Long userId);

	List<Staff> getStaffByDepartment(Long departmentId);

	List<Staff> getStaffByPosition(Position position);

	List<Staff> getStaffWithShiftAssignments(Long departmentId);

	Staff updateStaffInfo(Long userId, StaffUpdateRequest staffUpdateRequest);

	List<Staff> getAllStaff();

	Optional<Staff> getStaffById(Long id);
}