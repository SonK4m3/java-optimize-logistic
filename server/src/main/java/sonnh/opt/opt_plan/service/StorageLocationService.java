package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.StorageLocationDTO;
import sonnh.opt.opt_plan.payload.request.CreateStorageLocationRequest;
import sonnh.opt.opt_plan.payload.request.FindStorageLocationRequest;

import java.util.List;

public interface StorageLocationService {
	StorageLocationDTO createStorageLocation(CreateStorageLocationRequest request);

	List<StorageLocationDTO> findAvailableLocations(FindStorageLocationRequest request);

	StorageLocationDTO getStorageLocationById(Long id);

	void markLocationAsOccupied(Long id);

	void markLocationAsAvailable(Long id);

	List<StorageLocationDTO> getLocationsByWarehouse(Long warehouseId);
}