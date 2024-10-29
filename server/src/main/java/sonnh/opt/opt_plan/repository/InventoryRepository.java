package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.model.Warehouse;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	List<Inventory> findByWarehouse(Warehouse warehouse);

	List<Inventory> findByProduct(Product product);

	Optional<Inventory> findByWarehouseAndProduct(Warehouse warehouse, Product product);

	@Query("SELECT i FROM Inventory i WHERE i.warehouse = :warehouse AND i.quantity < i.minQuantity")
	List<Inventory> findLowStockByWarehouse(Warehouse warehouse);
}