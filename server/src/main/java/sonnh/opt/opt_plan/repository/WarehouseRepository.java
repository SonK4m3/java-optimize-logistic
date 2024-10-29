package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Warehouse;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	Optional<Warehouse> findByCode(String code);

	List<Warehouse> findByIsActive(Boolean isActive);

	boolean existsByCode(String code);
}