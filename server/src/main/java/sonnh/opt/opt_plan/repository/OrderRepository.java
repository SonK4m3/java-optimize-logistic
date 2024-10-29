package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);

	List<Order> findByStatus(OrderStatus status);

	List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	Optional<Order> findByOrderCode(String orderCode);
}