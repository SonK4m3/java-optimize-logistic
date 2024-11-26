package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sonnh.opt.opt_plan.model.StorageLocation;
import sonnh.opt.opt_plan.constant.enums.StorageLocationType;

import java.util.List;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
	/**
	 * Find available storage locations in a warehouse that can handle the
	 * required weight
	 * 
	 * @param warehouseId    ID of the warehouse to search in
	 * @param requiredWeight Minimum weight capacity required
	 * @return List of available storage locations meeting the weight
	 *         requirement
	 */
	@Query("SELECT sl FROM StorageLocation sl " + "JOIN sl.storageArea sa "
			+ "WHERE sa.warehouse.id = :warehouseId "
			+ "AND sl.maxWeight >= :requiredWeight " + "AND sl.isOccupied = false "
			+ "ORDER BY sl.level ASC, sl.position ASC")
	List<StorageLocation> findAvailableLocations(Long warehouseId, Double requiredWeight);

	/**
	 * Find available storage locations in a warehouse with detailed criteria
	 * filtering
	 * 
	 * @param warehouseId    ID of the warehouse to search in
	 * @param requiredWeight Minimum weight capacity required
	 * @param type           Type of storage location (optional)
	 * @param minHeight      Minimum height required (optional)
	 * @param minWidth       Minimum width required (optional)
	 * @param minLength      Minimum length required (optional)
	 * @return List of filtered storage locations ordered by level and position
	 */
	@Query("SELECT sl FROM StorageLocation sl " + "JOIN sl.storageArea sa "
			+ "WHERE sa.warehouse.id = :warehouseId " + "AND sl.isOccupied = false "
			+ "AND sl.maxWeight >= :requiredWeight "
			+ "AND (:type is null OR sl.type = :type) "
			+ "AND (:minHeight is null OR sl.height >= :minHeight) "
			+ "AND (:minWidth is null OR sl.width >= :minWidth) "
			+ "AND (:minLength is null OR sl.length >= :minLength) "
			+ "ORDER BY sl.level ASC, sl.position ASC")
	List<StorageLocation> findAvailableLocationsWithCriteria(Long warehouseId,
			Double requiredWeight, StorageLocationType type, Double minHeight,
			Double minWidth, Double minLength);
}