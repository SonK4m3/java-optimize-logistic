package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Task;
import sonnh.opt.opt_plan.constant.enums.TaskPriority;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository
		extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

	/**
	 * Find tasks by status
	 */
	List<Task> findByStatus(TaskStatus status);

	/**
	 * Find tasks by priority and status
	 */
	List<Task> findByPriorityAndStatus(TaskPriority priority, TaskStatus status);

	/**
	 * Find tasks within date range
	 */
	@Query("SELECT t FROM Task t WHERE t.startTime >= :startDate AND t.endTime <= :endDate")
	List<Task> findTasksInDateRange(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	/**
	 * Find tasks assigned to staff
	 */
	@Query("SELECT t FROM Task t JOIN t.assignments a WHERE a.staff.id = :staffId")
	List<Task> findTasksByStaffId(@Param("staffId") Long staffId);

	/**
	 * Find overdue tasks
	 */
	@Query("SELECT t FROM Task t WHERE t.endTime < CURRENT_TIMESTAMP AND t.status NOT IN ('COMPLETED', 'CANCELLED')")
	List<Task> findOverdueTasks();

	/**
	 * Find tasks by title
	 */
	List<Task> findByTitle(String title);
}