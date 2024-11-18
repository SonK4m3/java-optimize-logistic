package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByIsActive(Boolean isActive);

	List<Product> findByIsActiveTrue();
}