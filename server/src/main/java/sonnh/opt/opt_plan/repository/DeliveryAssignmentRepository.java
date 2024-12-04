package sonnh.opt.opt_plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.constant.enums.DeliveryAssignmentStatus;

@Repository
public interface DeliveryAssignmentRepository
		extends JpaRepository<DeliveryAssignment, Long> {

	@Query("""
			SELECT da FROM DeliveryAssignment da
			WHERE da.delivery.id = :deliveryId
			AND da.driver.id = :driverId
			AND da.status = :status""")
	List<DeliveryAssignment> findByDeliveryIdAndDriverIdAndStatus(Long deliveryId,
			Long driverId, DeliveryAssignmentStatus status);

	@Query("""
			SELECT da FROM DeliveryAssignment da
			WHERE da.driver.id = :driverId
			AND da.status IN :statuses""")
	List<DeliveryAssignment> findByDriverIdAndStatusIn(Long driverId,
			List<DeliveryAssignmentStatus> statuses);

	@Query("""
			SELECT da FROM DeliveryAssignment da
			WHERE da.delivery.id = :deliveryId
			AND da.status IN :statuses""")
	List<DeliveryAssignment> findByDeliveryIdAndStatusIn(Long deliveryId,
			List<DeliveryAssignmentStatus> statuses);

	@Query("""
			SELECT da FROM DeliveryAssignment da
			WHERE da.delivery.id = :deliveryId
			AND da.driver.id = :driverId""")
	List<DeliveryAssignment> findByDeliveryIdAndDriverId(Long deliveryId, Long driverId);

	@Query("""
			SELECT da FROM DeliveryAssignment da
			WHERE da.driver.id = :driverId
			ORDER BY da.assignedAt DESC""")
	List<DeliveryAssignment> findByDriverId(Long driverId);

	@Query("""
			SELECT da FROM DeliveryAssignment da
			WHERE da.delivery.id = :deliveryId
			ORDER BY da.assignedAt DESC""")
	List<DeliveryAssignment> findByDeliveryId(Long deliveryId);
}