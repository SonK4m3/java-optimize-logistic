package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Warehouse;
import java.util.List;

public interface WarehouseService {
	Warehouse createWarehouse(Warehouse warehouse);

	Warehouse updateWarehouse(Long id, Warehouse warehouse);

	Warehouse getWarehouseById(Long id);

	Warehouse getWarehouseByCode(String code);

	List<Warehouse> getAllWarehouses();

	List<Warehouse> getActiveWarehouses();

	void deleteWarehouse(Long id);

	boolean existsByCode(String code);
}