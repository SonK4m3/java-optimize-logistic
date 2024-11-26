package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.model.StorageLocation;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	List<Inventory> findByStorageLocation(StorageLocation storageLocation);

	List<Inventory> findByStorageLocationIn(List<StorageLocation> storageLocations);

	List<Inventory> findByProduct(Product product);

	@Query("SELECT i FROM Inventory i WHERE i.storageLocation.id = :storageLocationId AND i.product.id = :productId")
	Optional<Inventory> findByStorageLocationAndProduct(Long storageLocationId,
			Long productId);
}