package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.service.DriverService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
	private final DriverRepository driverRepository;

	@Override
	@Transactional
	public DriverDTO createDriver(DriverCreateRequest request) {
		Driver driver = Driver.builder()
				.driverCode("DRV-"
						+ UUID.randomUUID().toString().substring(0, 8).toUpperCase())
				.fullName(request.getFullName()).phone(request.getPhone())
				.email(request.getEmail()).licenseNumber(request.getLicenseNumber())
				.status(DriverStatus.AVAILABLE)
				.type(VehicleType.valueOf(request.getVehicleType()))
				.vehiclePlateNumber(request.getVehiclePlateNumber())
				.vehicleCapacity(request.getVehicleCapacity())
				.workStartTime(LocalTime.parse(request.getWorkStartTime()))
				.workEndTime(LocalTime.parse(request.getWorkEndTime()))
				.preferredAreas(request.getPreferredAreas())
				.maxDeliveryRadius(request.getMaxDeliveryRadius())
				.baseRate(request.getBaseRate()).ratePerKm(request.getRatePerKm())
				.build();

		driver = driverRepository.save(driver);
		return DriverDTO.fromEntity(driver);
	}

	@Override
	public List<DriverDTO> getAllDrivers() {
		return driverRepository.findAll().stream().map(DriverDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getDriversByStatus(DriverStatus status) {
		return driverRepository.findByStatus(status).stream().map(DriverDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getAvailableDriversNearby(Double latitude, Double longitude,
			Double radius) {
		return driverRepository.findAvailableDriversNearby(latitude, longitude, radius)
				.stream().map(DriverDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getDriversNearingEndOfShift(Integer minutes) {
		return driverRepository.findDriversNearingEndOfShift(minutes).stream()
				.map(DriverDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public DriverDTO updateDriverLocation(Long id, Double latitude, Double longitude) {
		Driver driver = getDriverEntity(id);
		driver.setCurrentLatitude(latitude);
		driver.setCurrentLongitude(longitude);
		driver.setLastLocationUpdate(LocalDateTime.now());
		driver = driverRepository.save(driver);
		return DriverDTO.fromEntity(driver);
	}

	@Override
	@Transactional
	public DriverDTO updateDriverStatus(Long id, DriverStatus status) {
		Driver driver = getDriverEntity(id);
		driver.setStatus(status);
		driver = driverRepository.save(driver);
		return DriverDTO.fromEntity(driver);
	}

	private Driver getDriverEntity(Long id) {
		return driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));
	}
}