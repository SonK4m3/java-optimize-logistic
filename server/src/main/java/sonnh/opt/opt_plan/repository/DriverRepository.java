package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.model.Driver;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	List<Driver> findByStatus(DriverStatus status);

	@Query("SELECT d FROM Driver d WHERE d.user.id = :userId")
	Driver findByUserId(@Param("userId") Long userId);

	@Query("SELECT d FROM Driver d WHERE d.status = :status AND d.vehicleType = :vehicleType")
	List<Driver> findByStatusAndVehicleType(@Param("status") DriverStatus status,
			@Param("vehicleType") VehicleType vehicleType);
}