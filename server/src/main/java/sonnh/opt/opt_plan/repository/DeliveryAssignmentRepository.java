package sonnh.opt.opt_plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

@Repository
public interface DeliveryAssignmentRepository
		extends JpaRepository<DeliveryAssignment, Long> {
	Optional<DeliveryAssignment> findByDeliveryIdAndDriverIdAndStatus(Long deliveryId,
			Long driverId, DeliveryStatus status);

	List<DeliveryAssignment> findByDriverIdAndStatusIn(Long driverId,
			List<DeliveryStatus> statuses);

	List<DeliveryAssignment> findByDeliveryIdAndStatusIn(Long deliveryId,
			List<DeliveryStatus> statuses);

	Optional<DeliveryAssignment> findByDeliveryIdAndDriverId(Long deliveryId,
			Long driverId);

	List<DeliveryAssignment> findByDriverId(Long driverId);
}