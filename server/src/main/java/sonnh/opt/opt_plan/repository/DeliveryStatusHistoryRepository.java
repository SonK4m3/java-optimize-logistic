package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.DeliveryStatusHistory;

@Repository
public interface DeliveryStatusHistoryRepository
		extends JpaRepository<DeliveryStatusHistory, Long> {}