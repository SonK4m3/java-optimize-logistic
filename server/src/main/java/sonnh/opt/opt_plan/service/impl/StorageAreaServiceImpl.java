package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.StorageArea;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.payload.dto.StorageAreaDTO;
import sonnh.opt.opt_plan.repository.StorageAreaRepository;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.service.StorageAreaService;
import sonnh.opt.opt_plan.constant.enums.StorageAreaType;
import sonnh.opt.opt_plan.payload.request.CreateStorageAreaRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageAreaServiceImpl implements StorageAreaService {
	private final StorageAreaRepository storageAreaRepository;
	private final WarehouseRepository warehouseRepository;

	@Override
	@Transactional
	public StorageAreaDTO createStorageArea(CreateStorageAreaRequest request) {
		Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

		if (!hasArea(warehouse, request.getArea())) {
			throw new ResourceNotFoundException(
					"Warehouse does not have enough capacity");
		}

		StorageArea storageArea = StorageArea.builder().name(request.getName())
				.type(request.getType()).area(request.getArea()).currentOccupancy(0)
				.isActive(true).warehouse(warehouse).build();

		return mapToDTO(storageAreaRepository.save(storageArea));
	}

	@Override
	public StorageAreaDTO getStorageAreaById(Long id) {
		return storageAreaRepository.findById(id).map(this::mapToDTO).orElseThrow(
				() -> new ResourceNotFoundException("Storage area not found"));
	}

	@Override
	public List<StorageAreaDTO> getStorageAreasByWarehouse(Long warehouseId) {
		return storageAreaRepository.findByWarehouseId(warehouseId).stream()
				.map(this::mapToDTO).toList();
	}

	@Override
	public List<StorageAreaDTO> getAvailableAreas(Long warehouseId) {
		return storageAreaRepository.findAvailableAreas(warehouseId).stream()
				.map(this::mapToDTO).toList();
	}

	@Override
	public List<StorageAreaDTO> getAreasByType(Long warehouseId, StorageAreaType type) {
		return storageAreaRepository
				.findByWarehouseIdAndTypeAndIsActiveTrue(warehouseId, type).stream()
				.map(this::mapToDTO).toList();
	}

	@Override
	@Transactional
	public void deactivateStorageArea(Long id) {
		StorageArea area = storageAreaRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Storage area not found"));
		area.setIsActive(false);
		storageAreaRepository.save(area);
	}

	private StorageAreaDTO mapToDTO(StorageArea area) {
		double utilizationRate = area.getArea() > 0
				? (double) area.getCurrentOccupancy() / area.getArea() * 100
				: 0;
		return StorageAreaDTO.fromEntity(area, utilizationRate);
	}

	private boolean hasArea(Warehouse warehouse, Integer area) {
		return warehouse.getStorageAreas().stream().mapToInt(StorageArea::getArea).sum()
				+ area <= warehouse.getTotalArea();
	}
}