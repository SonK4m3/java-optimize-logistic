package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.payload.request.DriverCreateByManagerRequest;
import org.springframework.data.domain.Page;
import java.util.List;

public interface DriverService {
	Driver createDriver(DriverCreateRequest request);

	Driver createDriverByManager(DriverCreateByManagerRequest request);

	Page<Driver> getAllDrivers(int page, int size);

	List<Driver> getDriversByStatus(DriverStatus status);

	List<Driver> getAvailableDriversNearby(Double latitude, Double longitude,
			Double radius);

	List<Driver> getDriversNearingEndOfShift(Integer minutes);

	Driver updateDriverLocation(Long id, Double latitude, Double longitude);

	Driver updateDriverStatus(Long id, DriverStatus status);
}