package sonnh.opt.opt_plan.service;

import java.util.List;
import java.util.Optional;
import sonnh.opt.opt_plan.constant.enums.VehicleStatus;
import sonnh.opt.opt_plan.payload.dto.VehicleDTO;
import sonnh.opt.opt_plan.payload.request.VehicleRequest;

public interface VehicleService {
	/**
	 * Get all available vehicles
	 * 
	 * @return List of available vehicle DTOs
	 */
	List<VehicleDTO> getAvailableVehicles();

	/**
	 * Create new vehicle
	 * 
	 * @param request Vehicle creation request
	 * @return Created vehicle DTO
	 */
	Optional<VehicleDTO> createVehicle(VehicleRequest request);

	/**
	 * Update vehicle status and location
	 * 
	 * @param vehicleId Vehicle ID
	 * @param status    New status
	 * @param latitude  Current latitude
	 * @param longitude Current longitude
	 * @return Updated vehicle DTO
	 */
	Optional<VehicleDTO> updateVehicleStatus(Long vehicleId, VehicleStatus status,
			Double latitude, Double longitude);

	/**
	 * Get vehicle by ID
	 * 
	 * @param id Vehicle ID
	 * @return Vehicle DTO if found
	 */
	Optional<VehicleDTO> getVehicleById(Long id);
}