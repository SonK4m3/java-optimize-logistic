package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.UserRole;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.payload.request.DriverCreateByManagerRequest;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.service.DriverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
	private final DriverRepository driverRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public Driver createDriver(DriverCreateRequest request) {
		// Find user by ID or throw exception if not found
		User user = userRepository.findById(request.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", request.getUserId()));

		// Build driver entity with basic required fields from request
		Driver driver = Driver.builder().user(user).phone(request.getPhone())
				.licenseNumber(request.getLicenseNumber()).status(DriverStatus.OFF_DUTY)
				.vehicleType(VehicleType.valueOf(request.getVehicleType()))
				.vehiclePlate(request.getVehiclePlateNumber()).build();

		// Save driver to database
		driver = driverRepository.save(driver);

		// Convert to DTO and return
		return driver;
	}

	@Override
	public Page<Driver> getAllDrivers(int page, int size) {
		return driverRepository.findAll(PageRequest.of(page, size));
	}

	@Override
	public List<Driver> getDriversByStatus(DriverStatus status) {
		return driverRepository.findByStatus(status);
	}

	@Override
	public List<Driver> getAvailableDriversNearby(Double latitude, Double longitude,
			Double radius) {
		return driverRepository.findAvailableDriversNearby(latitude, longitude, radius);
	}

	@Override
	public List<Driver> getDriversNearingEndOfShift(Integer minutes) {
		return driverRepository.findDriversNearingEndOfShift(minutes);
	}

	@Override
	@Transactional
	public Driver updateDriverLocation(Long id, Double latitude, Double longitude) {
		Driver driver = getDriverEntity(id);
		driver.setCurrentLatitude(latitude);
		driver.setCurrentLongitude(longitude);
		driver.setLastLocationUpdate(LocalDateTime.now());
		driver = driverRepository.save(driver);
		return driver;
	}

	@Override
	@Transactional
	public Driver updateDriverStatus(Long id, DriverStatus status) {
		Driver driver = getDriverEntity(id);
		driver.setStatus(status);
		driver = driverRepository.save(driver);
		return driver;
	}

	private Driver getDriverEntity(Long id) {
		return driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));
	}

	@Override
	@Transactional
	public Driver createDriverByManager(DriverCreateByManagerRequest request) {
		User user = User.builder().username(request.getUsername())
				.email(request.getEmail()).fullName(request.getFullName())
				.password(encoder.encode(request.getPassword())).role(UserRole.DRIVER)
				.isActive(true).build();

		user = userRepository.save(user);

		Driver driver = Driver.builder().user(user).phone(request.getPhone())
				.licenseNumber(request.getLicenseNumber())
				.vehicleType(request.getVehicleType())
				.vehiclePlate(request.getVehiclePlateNumber())
				.status(DriverStatus.OFF_DUTY).currentLatitude(0.0).currentLongitude(0.0)
				.build();

		driver = driverRepository.save(driver);

		return driver;
	}
}