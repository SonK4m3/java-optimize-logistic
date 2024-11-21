package sonnh.opt.opt_plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.ShiftAssignment;
import sonnh.opt.opt_plan.constant.enums.ShiftStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {

	/**
	 * Find assignments by staff and date range
	 */
	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.staff.id = :staffId "
			+ "AND sa.workDate BETWEEN :startDate AND :endDate")
	List<ShiftAssignment> findByStaffAndDateRange(@Param("staffId") Long staffId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	/**
	 * Find assignments by shift and date
	 */
	List<ShiftAssignment> findByWorkShiftIdAndWorkDate(Long shiftId, LocalDate workDate);

	/**
	 * Find assignments by status
	 */
	List<ShiftAssignment> findByStatus(ShiftStatus status);

	/**
	 * Find assignments by department
	 */
	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.staff.department.id = :departmentId "
			+ "AND sa.workDate BETWEEN :startDate AND :endDate")
	List<ShiftAssignment> findByDepartmentAndDateRange(
			@Param("departmentId") Long departmentId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	/**
	 * Check if staff is already assigned to any shift on date
	 */
	boolean existsByStaffIdAndWorkDate(Long staffId, LocalDate workDate);

	/**
	 * Count assignments by status and date
	 */
	@Query("SELECT COUNT(sa) FROM ShiftAssignment sa WHERE sa.status = :status "
			+ "AND sa.workDate = :workDate")
	Long countByStatusAndDate(@Param("status") ShiftStatus status,
			@Param("workDate") LocalDate workDate);

	/**
	 * Find assignments by workshift, date and staff ids Used to check for
	 * existing assignments before batch creation
	 */
	@Query("SELECT sa FROM ShiftAssignment sa " + "WHERE sa.workShift.id = :workShiftId "
			+ "AND sa.workDate = :workDate " + "AND sa.staff.id IN :staffIds")
	List<ShiftAssignment> findByWorkShiftIdAndWorkDateAndStaffIdIn(
			@Param("workShiftId") Long workShiftId, @Param("workDate") LocalDate workDate,
			@Param("staffIds") List<Long> staffIds);

	/**
	 * Find assignments by staff ID
	 */
	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.staff.id = :staffId")
	Page<ShiftAssignment> findByStaffId(@Param("staffId") Long staffId,
			Pageable pageable);

	/**
	 * Find assignments by work shift ID
	 */
	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.workShift.id = :workShiftId")
	Page<ShiftAssignment> findByWorkShiftId(@Param("workShiftId") Long workShiftId,
			Pageable pageable);
}