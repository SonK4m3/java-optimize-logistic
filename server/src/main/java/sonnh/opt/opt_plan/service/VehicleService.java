package sonnh.opt.opt_plan.service;

import java.util.List;
import java.util.Optional;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.payload.dto.VehicleDTO;
import sonnh.opt.opt_plan.payload.request.VehicleRequest;

public interface VehicleService {
	List<VehicleDTO> getAvailableVehicles();

	Optional<VehicleDTO> createVehicle(VehicleRequest request);

	Optional<VehicleDTO> updateVehicleStatus(Long vehicleId, VehicleStatus status,
			Double latitude, Double longitude);

	Optional<VehicleDTO> getVehicleById(Long id);
}