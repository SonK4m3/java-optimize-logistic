package sonnh.opt.opt_plan.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sonnh.opt.opt_plan.model.Route;
import sonnh.opt.opt_plan.model.Vehicle;
import sonnh.opt.opt_plan.constant.enums.RouteStatus;
import sonnh.opt.opt_plan.repository.projection.RouteStatusCount;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
	/**
	 * Find routes by vehicle and status
	 */
	List<Route> findByVehicleAndStatus(Vehicle vehicle, RouteStatus status);

	/**
	 * Find active routes (PLANNED or IN_PROGRESS)
	 */
	@Query("SELECT r FROM Route r WHERE r.status IN ('PLANNED', 'IN_PROGRESS')")
	List<Route> findActiveRoutes();

	/**
	 * Find routes with specific vehicle
	 */
	@Query("SELECT r FROM Route r " + "WHERE r.vehicle.id = :vehicleId "
			+ "AND r.status IN :statuses")
	List<Route> findVehicleRoutes(@Param("vehicleId") Long vehicleId,
			@Param("statuses") List<RouteStatus> statuses);

	/**
	 * Count routes by status
	 */
	@Query("SELECT r.status as status, COUNT(r) as count "
			+ "FROM Route r GROUP BY r.status")
	List<RouteStatusCount> countByStatus();
}