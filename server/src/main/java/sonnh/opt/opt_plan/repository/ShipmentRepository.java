package sonnh.opt.opt_plan.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sonnh.opt.opt_plan.model.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
	Optional<Shipment> findByCode(String code);

	@Query("SELECT s FROM Shipment s WHERE s.warehouse.id = :warehouseId")
	Page<Shipment> findAllByWarehouseId(Long warehouseId, Pageable pageable);

	boolean existsByCode(String code);
}