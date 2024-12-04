package sonnh.opt.opt_plan.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sonnh.opt.opt_plan.constant.enums.ReceiptStatus;
import sonnh.opt.opt_plan.constant.enums.ReceiptType;
import sonnh.opt.opt_plan.model.WarehouseReceipt;

@Repository
public interface WarehouseReceiptRepository
		extends JpaRepository<WarehouseReceipt, Long> {

	Optional<WarehouseReceipt> findByCode(String code);

	Page<WarehouseReceipt> findByStorageLocationIdAndType(Long storageLocationId,
			ReceiptType type, Pageable pageable);

	Page<WarehouseReceipt> findByStorageLocationId(Long storageLocationId,
			Pageable pageable);

	@Query("SELECT wr FROM WarehouseReceipt wr WHERE wr.status = :status "
			+ "AND wr.receiptDate BETWEEN :startDate AND :endDate")
	List<WarehouseReceipt> findByStatusAndDateRange(@Param("status") ReceiptStatus status,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	List<WarehouseReceipt> findByCreatedById(Long userId);

	long countByTypeAndStatus(ReceiptType type, ReceiptStatus status);

	boolean existsByCode(String code);
}