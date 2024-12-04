package sonnh.opt.opt_plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByOrderCode(String orderCode);

	@Query("SELECT o FROM Order o WHERE o.customer.id = :customerId "
			+ "AND o.createdAt BETWEEN :startDate AND :endDate")
	List<Order> findByCustomerAndDateRange(@Param("customerId") Long customerId,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Query("SELECT o FROM Order o " + "LEFT JOIN FETCH o.orderDetails "
			+ "LEFT JOIN FETCH o.delivery " + "WHERE o.id = :orderId")
	Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);

	List<Order> findByStatusAndCreatedAtBefore(OrderStatus status,
			LocalDateTime dateTime);

	@Query("SELECT COUNT(o) FROM Order o " + "WHERE o.status = :status "
			+ "AND o.customer.id = :customerId")
	long countByStatusAndCustomer(@Param("status") OrderStatus status,
			@Param("customerId") Long customerId);

	@Query("SELECT o FROM Order o " + "LEFT JOIN FETCH o.orderDetails "
			+ "LEFT JOIN FETCH o.delivery " + "WHERE o.customer.id = :customerId")
	Page<Order> findOrdersByCustomerId(@Param("customerId") Long customerId,
			Pageable pageable);
}