package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;
import sonnh.opt.opt_plan.model.VRPTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VRPTaskRepository extends JpaRepository<VRPTask, Long> {
	Optional<VRPTask> findByTaskId(String taskId);

	List<VRPTask> findByStatus(TaskStatus status);

	@Query("SELECT t FROM VRPTask t WHERE t.status = 'PROCESSING' AND t.startedAt < :timeout")
	List<VRPTask> findStuckTasks(LocalDateTime timeout);

	@Query("SELECT t FROM VRPTask t WHERE t.status IN ('CREATED', 'QUEUED') ORDER BY t.createdAt ASC")
	List<VRPTask> findPendingTasks();
}