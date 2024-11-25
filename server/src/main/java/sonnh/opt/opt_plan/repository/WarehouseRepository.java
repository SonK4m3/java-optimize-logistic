
package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.constant.enums.WarehouseStatus;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	List<Warehouse> findByStatus(WarehouseStatus status);

	List<Warehouse> findByStatusAndType(WarehouseStatus status, WarehouseType type);
}