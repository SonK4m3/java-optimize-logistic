package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.StorageArea;
import sonnh.opt.opt_plan.model.StorageLocation;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.payload.dto.StorageLocationDTO;
import sonnh.opt.opt_plan.repository.StorageLocationRepository;
import sonnh.opt.opt_plan.repository.StorageAreaRepository;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.service.StorageLocationService;
import sonnh.opt.opt_plan.payload.request.CreateStorageLocationRequest;
import sonnh.opt.opt_plan.payload.request.FindStorageLocationRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageLocationServiceImpl implements StorageLocationService {
	private final StorageLocationRepository storageLocationRepository;
	private final WarehouseRepository warehouseRepository;
	private final StorageAreaRepository storageAreaRepository;

	@Override
	@Transactional
	public StorageLocationDTO createStorageLocation(
			CreateStorageLocationRequest request) {
		StorageArea storageArea = storageAreaRepository
				.findById(request.getStorageAreaId()).orElseThrow(
						() -> new ResourceNotFoundException("Storage area not found"));

		StorageLocation location = StorageLocation.builder().storageArea(storageArea)
				.type(request.getType()).length(request.getLength())
				.width(request.getWidth()).height(request.getHeight())
				.maxWeight(request.getMaxWeight()).level(request.getLevel())
				.position(request.getPosition()).isOccupied(false)
				.code(StorageLocation.generateLocationCode(request.getType(),
						request.getLevel(), request.getPosition()))
				.build();

		StorageLocation saved = storageLocationRepository.save(location);
		return StorageLocationDTO.fromEntity(saved);
	}

	@Override
	public List<StorageLocationDTO> findAvailableLocations(
			FindStorageLocationRequest request) {
		return storageLocationRepository
				.findAvailableLocationsWithCriteria(request.getWarehouseId(),
						request.getRequiredWeight(), request.getPreferredType(),
						request.getMinHeight(), request.getMinWidth(),
						request.getMinLength())
				.stream().map(StorageLocationDTO::fromEntity).toList();
	}

	@Override
	public StorageLocationDTO getStorageLocationById(Long id) {
		StorageLocation location = storageLocationRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Storage location not found"));
		return StorageLocationDTO.fromEntity(location);
	}

	@Override
	@Transactional
	public void markLocationAsOccupied(Long id) {
		StorageLocation location = storageLocationRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Storage location not found"));
		if (location.getIsOccupied()) {
			throw new IllegalStateException("Location is already occupied");
		}
		location.setIsOccupied(true);
		storageLocationRepository.save(location);
	}

	@Override
	@Transactional
	public void markLocationAsAvailable(Long id) {
		StorageLocation location = storageLocationRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Storage location not found"));
		if (!location.getIsOccupied()) {
			throw new IllegalStateException("Location is already available");
		}
		location.setIsOccupied(false);
		storageLocationRepository.save(location);
	}

	@Override
	public List<StorageLocationDTO> getLocationsByWarehouse(Long warehouseId) {
		Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
		List<StorageArea> storageAreas = storageAreaRepository.findByWarehouse(warehouse);
		return storageAreas.stream().flatMap(area -> area.getStorageLocations().stream())
				.map(StorageLocationDTO::fromEntity).toList();
	}
}