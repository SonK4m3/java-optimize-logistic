package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.WorkShift;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface WorkShiftRepository extends JpaRepository<WorkShift, Long> {

	/**
	 * Find active shifts
	 */
	List<WorkShift> findByIsActiveTrue();

	/**
	 * Find shifts by time range
	 */
	@Query("SELECT ws FROM WorkShift ws WHERE "
			+ "ws.startTime >= :startTime AND ws.endTime <= :endTime")
	List<WorkShift> findByTimeRange(@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime);

	/**
	 * Find overlapping shifts
	 */
	@Query("SELECT ws FROM WorkShift ws WHERE "
			+ "(:startTime BETWEEN ws.startTime AND ws.endTime OR "
			+ ":endTime BETWEEN ws.startTime AND ws.endTime)")
	List<WorkShift> findOverlappingShifts(@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime);

	/**
	 * Find shifts by name containing
	 */
	List<WorkShift> findByNameContainingIgnoreCase(String keyword);
}