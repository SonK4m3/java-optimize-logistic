package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.repository.InventoryRepository;
import sonnh.opt.opt_plan.service.InventoryService;
import sonnh.opt.opt_plan.payload.request.InventoryCreateRequest;
import java.time.LocalDateTime;
import java.util.List;

import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.repository.WarehouseRepository;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
	private final InventoryRepository inventoryRepository;
	private final WarehouseRepository warehouseRepository;

	/**
	 * Create new inventory
	 * 
	 * @param inventory inventory object to create
	 * @return created inventory
	 */
	@Override
	public Inventory createInventory(InventoryCreateRequest request) {
		Inventory inventory = new Inventory();
		inventory.setCreatedAt(LocalDateTime.now());
		inventory.setUpdatedAt(LocalDateTime.now());
		return inventoryRepository.save(inventory);
	}

	/**
	 * Get inventory by id
	 * 
	 * @param id inventory id
	 * @return inventory object
	 * @throws ResourceNotFoundException if inventory not found
	 */
	@Override
	public Inventory getInventoryById(Long id) {
		return inventoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Inventory not found with id: " + id));
	}

	/**
	 * Get all inventories
	 * 
	 * @return page of all inventories
	 */
	@Override
	public Page<Inventory> findAll(Pageable pageable) {
		return inventoryRepository.findAll(pageable);
	}

	@Override
	public List<Inventory> getInventoriesByWarehouseId(Long warehouseId) {
		Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Warehouse not found with id: " + warehouseId));
		return inventoryRepository.findByWarehouse(warehouse);
	}
}