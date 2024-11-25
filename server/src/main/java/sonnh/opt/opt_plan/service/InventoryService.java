package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.payload.request.InventoryCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
	Inventory createInventory(InventoryCreateRequest request);

	Inventory getInventoryById(Long id);

	Page<Inventory> findAll(Pageable pageable);

	List<Inventory> getInventoriesByWarehouseId(Long warehouseId);
}