package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Driver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	List<Delivery> findByStatus(DeliveryStatus status);

	List<Delivery> findByDriver(Driver driver);

	List<Delivery> findByDeliveryDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	Optional<Delivery> findByDeliveryCode(String deliveryCode);
}