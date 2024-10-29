package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.model.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	List<Vehicle> findByStatus(VehicleStatus status);

	List<Vehicle> findByDriver(Driver driver);

	Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

	List<Vehicle> findByType(String type);
}