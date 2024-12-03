package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.model.Route;
import sonnh.opt.opt_plan.constant.enums.RouteStatus;
import sonnh.opt.opt_plan.model.Vehicle;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.repository.RouteRepository;
import sonnh.opt.opt_plan.repository.VehicleRepository;
import sonnh.opt.opt_plan.service.VehicleService;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.payload.dto.VehicleDTO;
import sonnh.opt.opt_plan.payload.request.VehicleRequest;
import sonnh.opt.opt_plan.constant.enums.DeliveryStopStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
	private final VehicleRepository vehicleRepository;
	private final RouteRepository routeRepository;

	@Override
	@Transactional(readOnly = true)
	public List<VehicleDTO> getAvailableVehicles() {
		return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
				.map(VehicleDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Optional<VehicleDTO> createVehicle(VehicleRequest request) {
		Vehicle vehicle = Vehicle.builder().vehicleCode(request.getVehicleCode())
				.capacity(request.getCapacity()).costPerKm(request.getCostPerKm())
				.currentLat(request.getInitialLat()).currentLng(request.getInitialLng())
				.status(VehicleStatus.AVAILABLE).build();

		Vehicle savedVehicle = vehicleRepository.save(vehicle);
		return Optional.of(VehicleDTO.fromEntity(savedVehicle));
	}

	@Override
	@Transactional
	public Optional<VehicleDTO> updateVehicleStatus(Long vehicleId, VehicleStatus status,
			Double latitude, Double longitude) {
		Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
		if (vehicleOpt.isEmpty()) {
			throw new ResourceNotFoundException(
					"Vehicle not found with id: " + vehicleId);
		}

		Vehicle vehicle = vehicleOpt.get();
		vehicle.setStatus(status);
		vehicle.setCurrentLat(latitude);
		vehicle.setCurrentLng(longitude);

		Vehicle updatedVehicle = vehicleRepository.save(vehicle);
		return Optional.of(VehicleDTO.fromEntity(updatedVehicle));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<VehicleDTO> getVehicleById(Long id) {
		return vehicleRepository.findById(id).map(VehicleDTO::fromEntity);
	}
}