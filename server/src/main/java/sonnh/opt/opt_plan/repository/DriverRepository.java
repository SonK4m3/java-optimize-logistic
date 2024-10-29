package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.model.Driver;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
	List<Driver> findByStatus(DriverStatus status);

	Optional<Driver> findByPhone(String phone);

	Optional<Driver> findByLicenseNumber(String licenseNumber);
}