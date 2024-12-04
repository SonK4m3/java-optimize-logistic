package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sonnh.opt.opt_plan.model.DeliveryFee;
import java.util.Optional;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {
	Optional<DeliveryFee> findByDeliveryId(Long deliveryId);
}