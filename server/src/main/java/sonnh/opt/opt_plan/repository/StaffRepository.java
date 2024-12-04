package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.constant.enums.Position;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
	Optional<Staff> findByUser_Id(Long userId);

	@Query("SELECT s FROM Staff s WHERE s.department.id = :departmentId")
	List<Staff> findByDepartmentId(Long departmentId);

	List<Staff> findByPosition(Position position);

	@Query("SELECT s FROM Staff s LEFT JOIN FETCH s.shiftAssignments "
			+ "WHERE s.department.id = :departmentId")
	List<Staff> findByDepartmentWithShiftAssignments(Long departmentId);
}