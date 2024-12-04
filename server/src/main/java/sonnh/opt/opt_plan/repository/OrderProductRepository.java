package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sonnh.opt.opt_plan.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
