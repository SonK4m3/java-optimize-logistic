package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sonnh.opt.opt_plan.model.StorageArea;
import sonnh.opt.opt_plan.constant.enums.StorageAreaType;
import sonnh.opt.opt_plan.model.Warehouse;

import java.util.List;

public interface StorageAreaRepository extends JpaRepository<StorageArea, Long> {

	// Find all areas in a warehouse
	List<StorageArea> findByWarehouseId(Long warehouseId);

	List<StorageArea> findByWarehouse(Warehouse warehouse);

	// Find active areas by warehouse and type
	List<StorageArea> findByWarehouseIdAndTypeAndIsActiveTrue(Long warehouseId,
			StorageAreaType type);

	// Find available areas (not full)
	@Query("SELECT sa FROM StorageArea sa " + "WHERE sa.warehouse.id = :warehouseId "
			+ "AND sa.isActive = true " + "AND sa.currentOccupancy < sa.area")
	List<StorageArea> findAvailableAreas(@Param("warehouseId") Long warehouseId);
}