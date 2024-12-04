package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sonnh.opt.opt_plan.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	@Query("SELECT od FROM OrderDetail od " + "WHERE od.order.id = :orderId")
	List<OrderDetail> findByOrderId(@Param("orderId") Long orderId);

	@Query("SELECT SUM(od.quantity * od.price) FROM OrderDetail od "
			+ "WHERE od.order.id = :orderId")
	Double calculateOrderTotal(@Param("orderId") Long orderId);

	@Query("SELECT od.product.id, SUM(od.quantity) as total " + "FROM OrderDetail od "
			+ "WHERE od.order.createdAt >= :startDate " + "GROUP BY od.product.id "
			+ "ORDER BY total DESC")
	List<Object[]> findTopSellingProducts(@Param("startDate") LocalDateTime startDate);
}