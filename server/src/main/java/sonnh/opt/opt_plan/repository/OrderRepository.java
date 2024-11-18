package sonnh.opt.opt_plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Warehouse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findBySender(User sender);

	List<Order> findByStatus(OrderStatus status);

	List<Order> findByPriority(OrderPriority priority);

	List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	Optional<Order> findByOrderCode(String orderCode);

	List<Order> findByPickupWarehouse(Warehouse warehouse);

	@Query(value = "SELECT DISTINCT o FROM Order o " + "LEFT JOIN FETCH o.sender s "
			+ "WHERE s.id = :senderId", countQuery = "SELECT COUNT(DISTINCT o) FROM Order o WHERE o.sender.id = :senderId")
	Page<Order> findOrdersBySenderId(@Param("senderId") Long senderId, Pageable pageable);

	@Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.sender.id = :senderId")
	boolean hasOrders(@Param("senderId") Long senderId);

	@Query("SELECT o FROM Order o WHERE o.sender.id = :senderId")
	List<Order> findOrdersBySenderId(@Param("senderId") Long senderId);

	@Query("""
				SELECT o FROM Order o
				JOIN Delivery d ON d.order.id = o.id
				WHERE d.driver.id = :driverId
				AND o.status = 'COMPLETED'
				AND o.lastUpdated >= :date
				ORDER BY o.lastUpdated DESC
			""")
	List<Order> findCompletedOrdersByDriverAndDateAfter(@Param("driverId") Long driverId,
			@Param("date") LocalDateTime date);

	@Query("""
				SELECT o FROM Order o
				WHERE o.status = 'PENDING'
				AND o.receiverAddress IN :zones
				AND o.createdAt >= :minDate
				ORDER BY o.priority DESC, o.createdAt ASC
			""")
	List<Order> findAvailableOrdersInZones(@Param("zones") Set<String> zones,
			@Param("minDate") LocalDateTime minDate);
}