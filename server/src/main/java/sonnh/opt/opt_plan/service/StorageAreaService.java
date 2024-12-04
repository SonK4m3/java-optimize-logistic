package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.constant.enums.StorageAreaType;
import sonnh.opt.opt_plan.payload.dto.StorageAreaDTO;
import sonnh.opt.opt_plan.payload.request.CreateStorageAreaRequest;

import java.util.List;

public interface StorageAreaService {
	StorageAreaDTO createStorageArea(CreateStorageAreaRequest request);

	StorageAreaDTO getStorageAreaById(Long id);

	List<StorageAreaDTO> getStorageAreasByWarehouse(Long warehouseId);

	List<StorageAreaDTO> getAvailableAreas(Long warehouseId);

	List<StorageAreaDTO> getAreasByType(Long warehouseId, StorageAreaType type);

	void deactivateStorageArea(Long id);
}