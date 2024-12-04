package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.ReceiptDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {

	// Find all receipt details by warehouse receipt id
	List<ReceiptDetail> findByWarehouseReceiptId(Long receiptId);

	// Find all receipt details by product id
	List<ReceiptDetail> findByProductId(Long productId);

	// Find receipt detail by receipt id and product id
	Optional<ReceiptDetail> findByWarehouseReceiptIdAndProductId(Long receiptId,
			Long productId);

	// Custom query to get total quantity by product id
	@Query("SELECT SUM(rd.quantity) FROM ReceiptDetail rd WHERE rd.product.id = :productId")
	Integer getTotalQuantityByProductId(@Param("productId") Long productId);

	// Delete all receipt details by warehouse receipt id
	void deleteByWarehouseReceiptId(Long receiptId);
}