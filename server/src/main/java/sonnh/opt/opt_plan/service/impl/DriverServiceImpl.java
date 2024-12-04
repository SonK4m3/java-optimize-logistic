package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.UserRole;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.model.Location;
import sonnh.opt.opt_plan.payload.request.DriverCreateByManagerRequest;
import sonnh.opt.opt_plan.payload.request.DriverCreateRequest;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.service.DeliveryService;
import sonnh.opt.opt_plan.service.DriverService;
import sonnh.opt.opt_plan.service.UserService;
import sonnh.opt.opt_plan.service.OrderService;
import sonnh.opt.opt_plan.service.WarehouseService;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
	private final double MAX_PICKUP_DISTANCE = 10.0;
	private final double MAX_DELIVERY_DISTANCE = 50.0;

	private final DriverRepository driverRepository;
	private final PasswordEncoder encoder;

	private final UserService userService;
	private final DeliveryService deliveryService;
	private final OrderService orderService;
	private final WarehouseService warehouseService;

	@Override
	@Transactional
	public Driver createDriver(DriverCreateRequest request) {
		// Find user by ID or throw exception if not found
		User user = userService.getUserById(request.getUserId());

		// Build driver entity with basic required fields from request
		Driver driver = Driver.builder().user(user).phone(request.getPhone())
				.licenseNumber(request.getLicenseNumber())
				.vehicleType(VehicleType.valueOf(request.getVehicleType()))
				.vehiclePlate(request.getVehiclePlateNumber()).currentLatitude(null)
				.currentLongitude(null).build();

		return driverRepository.save(driver);
	}

	@Override
	public Page<Driver> getAllDrivers(int page, int size) {
		return driverRepository.findAll(PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public Driver getDriverOrThrow(Long id) {
		return driverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Driver> getDriversOrThrow(List<Long> driverIds) {
		return driverRepository.findAllById(driverIds);
	}

	@Override
	@Transactional
	public Driver updateDriverLocation(Long id, Double latitude, Double longitude) {
		Driver driver = getDriverOrThrow(id);
		driver.updateLocation(latitude, longitude);
		return driverRepository.save(driver);
	}

	@Override
	@Transactional
	public Driver updateDriverStatus(Long id, DriverStatus status) {
		Driver driver = getDriverOrThrow(id);
		driver.setStatus(status);
		return driverRepository.save(driver);
	}

	@Override
	@Transactional
	public Driver createDriverByManager(DriverCreateByManagerRequest request) {
		User user = User.builder().username(request.getUsername())
				.email(request.getEmail()).fullName(request.getFullName())
				.password(encoder.encode(request.getPassword())).role(UserRole.DRIVER)
				.isActive(true).build();

		user = userService.createUser(user);

		Driver driver = Driver.builder().user(user).phone(request.getPhone())
				.licenseNumber(request.getLicenseNumber())
				.vehicleType(request.getVehicleType())
				.vehiclePlate(request.getVehiclePlateNumber()).currentLatitude(0.0)
				.currentLongitude(0.0).build();

		return driverRepository.save(driver);
	}

	@Override
	public List<Driver> getAvailableDrivers() {
		return driverRepository.findByStatus(DriverStatus.READY_TO_ACCEPT_ORDERS);
	}

	@Override
	public List<Driver> getAvailableDriversForDelivery(Long deliveryId) {
		Delivery delivery = deliveryService.getDeliveryOrThrow(deliveryId);

		Order order = orderService.getOrderById(delivery.getOrder().getId());

		// Get order weight and delivery location
		Double orderWeight = order.getTotalWeight();
		Location deliveryLocation = delivery.getDeliveryLocation();

		// Get vehicle type if driver is assigned
		VehicleType vehicleType = VehicleType.getRequiredVehicleType(orderWeight);

		// Get warehouses and handle not found efficiently
		List<Warehouse> warehouses = warehouseService
				.getWarehousesOrThrow(delivery.getWarehouseList());

		return findAvailableDriversByVehicleType(vehicleType, deliveryLocation,
				warehouses, MAX_PICKUP_DISTANCE, MAX_DELIVERY_DISTANCE);
	}

	private List<Driver> findAvailableDriversByVehicleType(VehicleType vehicleType,
			Location deliveryLocation, List<Warehouse> warehouses,
			double maxPickupDistance, double maxDeliveryDistance) {
		List<Driver> drivers = driverRepository.findByStatusAndVehicleType(
				DriverStatus.READY_TO_ACCEPT_ORDERS, vehicleType);

		return drivers.stream()
				.filter(driver -> driver.getCurrentLatitude() != null
						&& driver.getCurrentLongitude() != null
						&& driver.getCurrentLatitude() != 0.0
						&& driver.getCurrentLongitude() != 0.0)
				.collect(Collectors.toList());
	}
}