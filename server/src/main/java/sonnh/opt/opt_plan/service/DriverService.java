package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.payload.request.DriverCreateByManagerRequest;

import java.util.List;

import org.springframework.data.domain.Page;

public interface DriverService {
	Driver createDriver(DriverCreateRequest request);

	Driver createDriverByManager(DriverCreateByManagerRequest request);

	Page<Driver> getAllDrivers(int page, int size);

	Driver updateDriverLocation(Long id, Double latitude, Double longitude);

	Driver updateDriverStatus(Long id, DriverStatus status);

	List<Driver> getAvailableDrivers();

	List<Driver> getAvailableDriversForDelivery(Long deliveryId);

	Driver getDriverOrThrow(Long id);

	List<Driver> getDriversOrThrow(List<Long> driverIds);
}