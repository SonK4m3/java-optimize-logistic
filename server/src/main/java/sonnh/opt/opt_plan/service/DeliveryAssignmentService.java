package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.model.DeliveryStatusHistory;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.repository.DeliveryAssignmentRepository;
import sonnh.opt.opt_plan.repository.DeliveryRepository;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.repository.DeliveryStatusHistoryRepository;
import sonnh.opt.opt_plan.payload.request.DeliveryAssignmentCreateRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryAssignmentService {
	private final DeliveryAssignmentRepository assignmentRepository;
	private final DeliveryRepository deliveryRepository;
	private final DriverRepository driverRepository;
	private final DeliveryStatusHistoryRepository statusHistoryRepository;

	/**
	 * Assign delivery to driver
	 */
	public DeliveryAssignment assignDeliveryToDriver(Long deliveryId, Long driverId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

		// Validate driver availability
		if (driver.getStatus() != DriverStatus.AVAILABLE) {
			throw new ResourceNotFoundException("Driver is not available");
		}

		// Create assignment
		DeliveryAssignment assignment = DeliveryAssignment.builder().delivery(delivery)
				.driver(driver).assignedAt(LocalDateTime.now())
				.status(DeliveryStatus.ASSIGNED)
				.expiresAt(LocalDateTime.now().plusMinutes(5)) // 5 minutes to
																// respond
				.build();

		// Update delivery status
		delivery.setStatus(DeliveryStatus.ASSIGNED);
		delivery.setDriver(driver);

		// Save assignment and update status history
		assignmentRepository.save(assignment);
		updateDeliveryStatus(delivery, DeliveryStatus.ASSIGNED,
				"Assigned to driver: " + driver.getId());

		return assignment;
	}

	/**
	 * Driver accepts delivery
	 */
	public DeliveryAssignment acceptDelivery(Long deliveryId, Long driverId) {
		DeliveryAssignment assignment = findActiveAssignment(deliveryId, driverId);

		if (assignment.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new ResourceNotFoundException("Assignment has expired");
		}

		assignment.setStatus(DeliveryStatus.PICKED_UP);
		assignment.setRespondedAt(LocalDateTime.now());

		Delivery delivery = assignment.getDelivery();
		delivery.setStatus(DeliveryStatus.PICKED_UP);

		Driver driver = assignment.getDriver();
		driver.setStatus(DriverStatus.BUSY);

		// Update status history
		updateDeliveryStatus(delivery, DeliveryStatus.PICKED_UP,
				"Accepted by driver: " + driverId);

		return assignmentRepository.save(assignment);
	}

	/**
	 * Driver rejects delivery
	 */
	public DeliveryAssignment rejectDelivery(Long deliveryId, Long driverId,
			String reason) {
		DeliveryAssignment assignment = findActiveAssignment(deliveryId, driverId);

		assignment.setStatus(DeliveryStatus.CANCELLED);
		assignment.setRejectionReason(reason);
		assignment.setRespondedAt(LocalDateTime.now());

		Delivery delivery = assignment.getDelivery();
		delivery.setStatus(DeliveryStatus.PENDING); // Reset to pending
		delivery.setDriver(null);

		// Update status history
		updateDeliveryStatus(delivery, DeliveryStatus.CANCELLED,
				"Rejected by driver: " + driverId + ". Reason: " + reason);

		return assignmentRepository.save(assignment);
	}

	/**
	 * Update delivery status during delivery process
	 */
	public void updateDeliveryStatus(Long deliveryId, Long driverId,
			DeliveryStatus newStatus) {
		DeliveryAssignment assignment = findActiveAssignment(deliveryId, driverId);
		Delivery delivery = assignment.getDelivery();

		validateStatusTransition(delivery.getStatus(), newStatus);

		delivery.setStatus(newStatus);
		updateDeliveryStatus(delivery, newStatus,
				"Status updated by driver: " + driverId);

		deliveryRepository.save(delivery);
	}

	private DeliveryAssignment findActiveAssignment(Long deliveryId, Long driverId) {
		return assignmentRepository
				.findByDeliveryIdAndDriverIdAndStatus(deliveryId, driverId,
						DeliveryStatus.ASSIGNED)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Active assignment not found"));
	}

	private void updateDeliveryStatus(Delivery delivery, DeliveryStatus status,
			String note) {
		DeliveryStatusHistory statusHistory = DeliveryStatusHistory.builder()
				.delivery(delivery).status(status)
				.location(delivery.getDeliveryLocation()).note(note)
				.timestamp(LocalDateTime.now()).updatedBy("SYSTEM").build();

		delivery.addStatusHistory(statusHistory);
	}

	private void validateStatusTransition(DeliveryStatus currentStatus,
			DeliveryStatus newStatus) {
		// Add validation logic for valid status transitions
		// Example: ACCEPTED -> PICKING_UP -> PICKED_UP -> IN_TRANSIT ->
		// DELIVERED
		// Throw BusinessException if invalid transition
	}

	public DeliveryAssignment createDeliveryAssignment(
			DeliveryAssignmentCreateRequest request) {
		Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		Driver driver = driverRepository.findById(request.getDriverId())
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

		if (driver.getStatus() != DriverStatus.AVAILABLE) {
			throw new ResourceNotFoundException("Driver is not available");
		}

		delivery.setStatus(DeliveryStatus.ASSIGNED);
		delivery = deliveryRepository.save(delivery);

		DeliveryAssignment assignment = DeliveryAssignment.builder().delivery(delivery)
				.driver(driver).warehouseIds(request.getWarehouseIds())
				.assignedAt(LocalDateTime.now()).status(DeliveryStatus.ASSIGNED)
				.rejectionReason(null).respondedAt(null)
				.expiresAt(LocalDateTime.now().plusMinutes(5)).build();

		return assignmentRepository.save(assignment);
	}

	public List<DeliveryAssignment> getByDriver(Long driverId) {
		return assignmentRepository.findByDriverId(driverId);
	}
}