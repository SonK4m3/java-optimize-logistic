package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Inventory;
import java.util.List;

public interface InventoryService {
	Inventory createInventory(Inventory inventory);

	Inventory updateInventory(Long id, Inventory inventory);

	Inventory updateQuantity(Long id, Integer quantity);

	Inventory getInventoryById(Long id);

	List<Inventory> getAllInventories();

	List<Inventory> getInventoriesByWarehouse(Long warehouseId);

	List<Inventory> getInventoriesByProduct(Long productId);

	List<Inventory> getLowStockByWarehouse(Long warehouseId);

	void deleteInventory(Long id);
}