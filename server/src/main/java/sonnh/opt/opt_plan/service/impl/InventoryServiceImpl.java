package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.repository.InventoryRepository;
import sonnh.opt.opt_plan.repository.ProductRepository;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.service.InventoryService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
	private final InventoryRepository inventoryRepository;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;

	@Override
	public Inventory createInventory(Inventory inventory) {
		inventory.setLastUpdated(LocalDateTime.now());
		return inventoryRepository.save(inventory);
	}

	@Override
	public Inventory updateInventory(Long id, Inventory inventory) {
		Inventory existingInventory = getInventoryById(id);
		existingInventory.setQuantity(inventory.getQuantity());
		existingInventory.setMinQuantity(inventory.getMinQuantity());
		existingInventory.setMaxQuantity(inventory.getMaxQuantity());
		existingInventory.setLocation(inventory.getLocation());
		existingInventory.setStatus(inventory.getStatus());
		existingInventory.setLastUpdated(LocalDateTime.now());
		return inventoryRepository.save(existingInventory);
	}

	@Override
	public Inventory updateQuantity(Long id, Integer quantity) {
		Inventory inventory = getInventoryById(id);
		inventory.setQuantity(quantity);
		inventory.setLastUpdated(LocalDateTime.now());
		return inventoryRepository.save(inventory);
	}

	@Override
	public Inventory getInventoryById(Long id) {
		return inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
	}

	@Override
	public List<Inventory> getAllInventories() {
		return inventoryRepository.findAll();
	}

	@Override
	public List<Inventory> getInventoriesByWarehouse(Long warehouseId) {
		Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));
		return inventoryRepository.findByWarehouse(warehouse);
	}

	@Override
	public List<Inventory> getInventoriesByProduct(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
		return inventoryRepository.findByProduct(product);
	}

	@Override
	public List<Inventory> getLowStockByWarehouse(Long warehouseId) {
		Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));
		return inventoryRepository.findLowStockByWarehouse(warehouse);
	}

	@Override
	public void deleteInventory(Long id) {
		Inventory inventory = getInventoryById(id);
		inventoryRepository.delete(inventory);
	}
}