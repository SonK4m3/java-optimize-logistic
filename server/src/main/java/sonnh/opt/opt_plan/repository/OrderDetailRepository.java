package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.model.OrderDetail;
import sonnh.opt.opt_plan.model.Product;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
	List<OrderDetail> findByOrder(Order order);

	List<OrderDetail> findByProduct(Product product);
}