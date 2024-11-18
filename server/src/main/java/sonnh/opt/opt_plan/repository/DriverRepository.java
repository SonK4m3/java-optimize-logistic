package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.model.Driver;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
	Optional<Driver> findByDriverCode(String driverCode);

	List<Driver> findByStatus(DriverStatus status);

	List<Driver> findByIsActive(Boolean isActive);

	@Query("""
				SELECT d FROM Driver d
				WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(d.currentLatitude)) *
				cos(radians(d.currentLongitude) - radians(:longitude)) +
				sin(radians(:latitude)) * sin(radians(d.currentLatitude)))) <= :radius
				AND d.status = 'AVAILABLE'
				AND d.isActive = true
			""")
	List<Driver> findAvailableDriversNearby(@Param("latitude") Double latitude,
			@Param("longitude") Double longitude, @Param("radius") Double radius);

	@Query("SELECT d FROM Driver d WHERE d.remainingWorkingMinutes <= :minutes AND d.status = 'BUSY'")
	List<Driver> findDriversNearingEndOfShift(@Param("minutes") Integer minutes);

	@Query("SELECT d FROM Driver d WHERE d.preferredAreas LIKE %:areaCode% AND d.status = 'AVAILABLE' AND d.isActive = true")
	List<Driver> findAvailableDriversByPreferredArea(@Param("areaCode") String areaCode);

	List<Driver> findByVehicleTypeAndStatus(String vehicleType, DriverStatus status);

	@Query("SELECT d FROM Driver d WHERE d.averageRating >= :rating AND d.isActive = true")
	List<Driver> findDriversByMinimumRating(@Param("rating") Double rating);

	Long countByStatusAndIsActiveTrue(DriverStatus status);
}