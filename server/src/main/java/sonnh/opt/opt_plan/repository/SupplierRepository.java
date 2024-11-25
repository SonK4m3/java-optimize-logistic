package sonnh.opt.opt_plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Supplier;
import sonnh.opt.opt_plan.constant.enums.SupplierStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	Optional<Supplier> findByName(String name);

	@Query("SELECT s FROM Supplier s WHERE "
			+ "(:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND "
			+ "(:status IS NULL OR s.status = :status)")
	Page<Supplier> findAll(@Param("keyword") String keyword,
			@Param("status") SupplierStatus status, Pageable pageable);

	List<Supplier> findByStatus(SupplierStatus status);

	boolean existsByName(String name);
}