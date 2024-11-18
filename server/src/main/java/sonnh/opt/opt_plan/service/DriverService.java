package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import java.util.List;

public interface DriverService {
	DriverDTO createDriver(DriverCreateRequest request);

	DriverDTO updateDriver(Long id, DriverCreateRequest request);

	DriverDTO getDriverById(Long id);

	DriverDTO getDriverByCode(String driverCode);

	List<DriverDTO> getAllDrivers();

	List<DriverDTO> getActiveDrivers();

	List<DriverDTO> getDriversByStatus(DriverStatus status);

	List<DriverDTO> getAvailableDriversNearby(Double latitude, Double longitude,
			Double radius);

	List<DriverDTO> getDriversNearingEndOfShift(Integer minutes);

	DriverDTO updateDriverLocation(Long id, Double latitude, Double longitude);

	DriverDTO updateDriverStatus(Long id, DriverStatus status);

	void deleteDriver(Long id);

	void updateWorkingHours(Long id);
}