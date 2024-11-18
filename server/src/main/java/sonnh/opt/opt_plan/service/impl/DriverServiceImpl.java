package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.service.DriverService;

import java.time.LocalDateTime;
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
				.status(DriverStatus.AVAILABLE).vehicleType(request.getVehicleType())
				.vehiclePlateNumber(request.getVehiclePlateNumber())
				.vehicleCapacity(request.getVehicleCapacity())
				.workStartTime(request.getWorkStartTime())
				.workEndTime(request.getWorkEndTime()).maxWorkingHours(8) // Default
																			// 8
																			// hours
				.remainingWorkingMinutes(480) // 8 hours in minutes
				.preferredAreas(request.getPreferredAreas())
				.maxDeliveryRadius(request.getMaxDeliveryRadius())
				.baseRate(request.getBaseRate()).ratePerKm(request.getRatePerKm())
				.totalDeliveries(0).completedDeliveries(0).cancelledDeliveries(0)
				.averageRating(0.0).totalReviews(0).createdAt(LocalDateTime.now())
				.isActive(true).build();

		driver = driverRepository.save(driver);
		return convertToDTO(driver);
	}

	@Override
	@Transactional
	public DriverDTO updateDriver(Long id, DriverCreateRequest request) {
		Driver driver = getDriverEntity(id);

		driver.setFullName(request.getFullName());
		driver.setPhone(request.getPhone());
		driver.setEmail(request.getEmail());
		driver.setLicenseNumber(request.getLicenseNumber());
		driver.setVehicleType(request.getVehicleType());
		driver.setVehiclePlateNumber(request.getVehiclePlateNumber());
		driver.setVehicleCapacity(request.getVehicleCapacity());
		driver.setWorkStartTime(request.getWorkStartTime());
		driver.setWorkEndTime(request.getWorkEndTime());
		driver.setPreferredAreas(request.getPreferredAreas());
		driver.setMaxDeliveryRadius(request.getMaxDeliveryRadius());
		driver.setBaseRate(request.getBaseRate());
		driver.setRatePerKm(request.getRatePerKm());
		driver.setUpdatedAt(LocalDateTime.now());

		driver = driverRepository.save(driver);
		return convertToDTO(driver);
	}

	@Override
	public DriverDTO getDriverById(Long id) { return convertToDTO(getDriverEntity(id)); }

	@Override
	public DriverDTO getDriverByCode(String driverCode) {
		Driver driver = driverRepository.findByDriverCode(driverCode).orElseThrow(
				() -> new ResourceNotFoundException("Driver", "code", driverCode));
		return convertToDTO(driver);
	}

	@Override
	public List<DriverDTO> getAllDrivers() {
		return driverRepository.findAll().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getActiveDrivers() {
		return driverRepository.findByIsActive(true).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getDriversByStatus(DriverStatus status) {
		return driverRepository.findByStatus(status).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getAvailableDriversNearby(Double latitude, Double longitude,
			Double radius) {
		return driverRepository.findAvailableDriversNearby(latitude, longitude, radius)
				.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<DriverDTO> getDriversNearingEndOfShift(Integer minutes) {
		return driverRepository.findDriversNearingEndOfShift(minutes).stream()
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public DriverDTO updateDriverLocation(Long id, Double latitude, Double longitude) {
		Driver driver = getDriverEntity(id);

		driver.setCurrentLatitude(latitude);
		driver.setCurrentLongitude(longitude);
		driver.setLastLocationUpdate(LocalDateTime.now());

		driver = driverRepository.save(driver);
		return convertToDTO(driver);
	}

	@Override
	@Transactional
	public DriverDTO updateDriverStatus(Long id, DriverStatus status) {
		Driver driver = getDriverEntity(id);

		driver.setStatus(status);
		driver.setUpdatedAt(LocalDateTime.now());

		if (status == DriverStatus.ON_BREAK) {
			driver.setLastBreakTime(LocalDateTime.now());
		}

		driver = driverRepository.save(driver);
		return convertToDTO(driver);
	}

	@Override
	@Transactional
	public void deleteDriver(Long id) {
		Driver driver = getDriverEntity(id);
		driver.setIsActive(false);
		driver.setStatus(DriverStatus.INACTIVE);
		driver.setUpdatedAt(LocalDateTime.now());
		driverRepository.save(driver);
	}

	@Override
	@Transactional
	public void updateWorkingHours(Long id) {
		Driver driver = getDriverEntity(id);

		// Calculate remaining working minutes based on start time and current
		// time
		int workedMinutes = calculateWorkedMinutes(driver);
		driver.setRemainingWorkingMinutes(
				driver.getMaxWorkingHours() * 60 - workedMinutes);

		driver = driverRepository.save(driver);
	}

	private Driver getDriverEntity(Long id) {
		return driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));
	}

	private DriverDTO convertToDTO(Driver driver) {
		return DriverDTO.builder().id(driver.getId()).driverCode(driver.getDriverCode())
				.fullName(driver.getFullName()).phone(driver.getPhone())
				.status(driver.getStatus()).vehicleType(driver.getVehicleType())
				.vehiclePlateNumber(driver.getVehiclePlateNumber())
				.workStartTime(driver.getWorkStartTime())
				.workEndTime(driver.getWorkEndTime())
				.remainingWorkingMinutes(driver.getRemainingWorkingMinutes())
				.preferredAreas(driver.getPreferredAreas())
				.completedDeliveries(driver.getCompletedDeliveries())
				.averageRating(driver.getAverageRating())
				.currentLatitude(driver.getCurrentLatitude())
				.currentLongitude(driver.getCurrentLongitude())
				.isActive(driver.getIsActive()).build();
	}

	private int calculateWorkedMinutes(Driver driver) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startTime = now.with(driver.getWorkStartTime());

		if (now.isBefore(startTime)) {
			return 0;
		}

		return (int) java.time.Duration.between(startTime, now).toMinutes();
	}
}