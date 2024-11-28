package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	@Query("SELECT d FROM Delivery d " + "WHERE d.driver.id = :driverId "
			+ "AND d.status IN :statuses")
	List<Delivery> findActiveDeliveriesByDriver(@Param("driverId") Long driverId,
			@Param("statuses") List<DeliveryStatus> statuses);

	@Query("SELECT d FROM Delivery d " + "WHERE d.status = :status "
			+ "AND d.estimatedDeliveryTime <= :deadline")
	List<Delivery> findDelayedDeliveries(@Param("status") DeliveryStatus status,
			@Param("deadline") LocalDateTime deadline);

	@Query("SELECT d FROM Delivery d " + "LEFT JOIN FETCH d.statusHistory "
			+ "WHERE d.id = :deliveryId")
	Optional<Delivery> findByIdWithHistory(@Param("deliveryId") Long deliveryId);

	List<Delivery> findByStatusAndDriverIsNull(DeliveryStatus status);

	List<Delivery> findByOrder(Order order);
}