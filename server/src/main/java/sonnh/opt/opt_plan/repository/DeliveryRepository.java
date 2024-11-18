package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.model.Delivery;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	@Query("SELECT d FROM Delivery d WHERE d.driver.id = :driverId")
	List<Delivery> findByDriverId(Long driverId);

	@Query("SELECT d FROM Delivery d WHERE d.order.id = :orderId")
	Optional<Delivery> findByOrderId(Long orderId);

	@Query("SELECT d FROM Delivery d WHERE d.status = :status")
	List<Delivery> findByStatus(DeliveryStatus status);

	@Query("""
				SELECT d FROM Delivery d
				WHERE d.driver.id = :driverId
				AND d.status = 'COMPLETED'
				AND d.deliveryDate >= :date
				ORDER BY d.deliveryDate DESC
			""")
	List<Delivery> findCompletedDeliveriesByDriverAndDateAfter(
			@Param("driverId") Long driverId, @Param("date") LocalDateTime date);
}