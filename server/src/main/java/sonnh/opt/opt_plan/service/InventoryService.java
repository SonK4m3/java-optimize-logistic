package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Inventory;
import java.util.List;

public interface InventoryService {
	Inventory createInventory(Inventory inventory);

	Inventory getInventoryById(Long id);

	List<Inventory> getAllInventories();
}