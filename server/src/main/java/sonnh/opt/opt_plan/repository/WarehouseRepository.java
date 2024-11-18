
package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sonnh.opt.opt_plan.model.Warehouse;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

	List<Warehouse> findByIsActiveTrue();

	@Query("SELECT w FROM Warehouse w WHERE w.isActive = true "
			+ "AND w.latitude BETWEEN :latitude - (:radius / 111.0) AND :latitude + (:radius / 111.0) "
			+ "AND w.longitude BETWEEN :longitude - (:radius / (111.0 * COS(RADIANS(:latitude)))) "
			+ "AND :longitude + (:radius / (111.0 * COS(RADIANS(:latitude))))")
	List<Warehouse> findNearbyWarehouses(@Param("latitude") Double latitude,
			@Param("longitude") Double longitude, @Param("radius") Double radius);
}