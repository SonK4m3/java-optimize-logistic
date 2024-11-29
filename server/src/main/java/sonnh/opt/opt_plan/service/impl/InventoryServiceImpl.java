package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.model.StorageArea;
import sonnh.opt.opt_plan.model.StorageLocation;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.repository.InventoryRepository;
import sonnh.opt.opt_plan.service.InventoryService;
import sonnh.opt.opt_plan.payload.request.InventoryCreateRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.transaction.annotation.Transactional;

import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
	private final InventoryRepository inventoryRepository;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;

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

		List<StorageArea> storageAreas = warehouse.getStorageAreas();

		List<StorageLocation> storageLocations = storageAreas.stream()
				.flatMap(area -> area.getStorageLocations().stream()).toList();

		return inventoryRepository.findByStorageLocationIn(storageLocations);
	}

	@Override
	@Transactional
	public Inventory decreaseStock(Long warehouseId, Long productId, int quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Product not found with id: " + productId));

		List<Inventory> inventories = getInventoriesByWarehouseId(warehouseId);

		Inventory inventory = inventories.stream()
				.filter(inv -> inv.getProduct().getId().equals(product.getId()))
				.findFirst().orElseThrow(() -> new ResourceNotFoundException(
						"Inventory not found with product id: " + productId));

		if (inventory.getQuantity() < quantity) {
			inventory.setQuantity(0);
		} else {
			inventory.setQuantity(inventory.getQuantity() - quantity);
		}

		return inventoryRepository.save(inventory);
	}

	@Override
	public List<Inventory> getInventoriesByProductId(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Product not found with id: " + productId));

		return inventoryRepository.findByProduct(product);
	}

	@Override
	public Integer getTotalQuantityByProductId(Long productId) {
		return getInventoriesByProductId(productId).stream()
				.mapToInt(Inventory::getQuantity).sum();
	}

	@Override
	public Boolean checkStockAvailability(Long productId, int quantity) {
		return getTotalQuantityByProductId(productId) >= quantity;
	}

	@Override
	@Transactional
	public List<Inventory> getOutForOrder(Long productId, int quantity) {
		List<Inventory> inventories = getInventoriesByProductId(productId);
		List<Inventory> selectedInventories = new ArrayList<>();
		int remainingQuantity = quantity;

		// Sort inventories by quantity ascending to optimize stock usage
		inventories.sort(Comparator.comparing(Inventory::getQuantity));

		for (Inventory inventory : inventories) {
			if (remainingQuantity <= 0) {
				break;
			}

			int availableQuantity = inventory.getQuantity();
			if (availableQuantity > 0) {
				if (availableQuantity >= remainingQuantity) {
					// This inventory can fulfill the remaining quantity
					inventory.setQuantity(availableQuantity - remainingQuantity);
					selectedInventories.add(inventory);
					remainingQuantity = 0;
				} else {
					// Take what we can from this inventory and continue to next
					inventory.setQuantity(0);
					selectedInventories.add(inventory);
					remainingQuantity -= availableQuantity;
				}
				inventoryRepository.save(inventory);
			}
		}

		if (remainingQuantity > 0) {
			throw new ResourceNotFoundException(
					"Insufficient stock available for product id: " + productId
							+ ". Missing quantity: " + remainingQuantity);
		}

		return selectedInventories;
	}
}