package sonnh.opt.opt_plan.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.DeliveryAssignmentStatus;
import sonnh.opt.opt_plan.exception.BusinessException;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.DeliveryAssignment;
import sonnh.opt.opt_plan.model.DeliveryStatusHistory;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.payload.request.DeliveryAssignmentCreateRequest;
import sonnh.opt.opt_plan.repository.DeliveryAssignmentRepository;
import sonnh.opt.opt_plan.repository.DeliveryRepository;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.repository.DeliveryStatusHistoryRepository;
import sonnh.opt.opt_plan.service.DeliveryAssignmentService;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService {
	private final Integer EXPIRATION_TIME_MINUTES = 10;
	private final DeliveryAssignmentRepository assignmentRepository;
	private final DeliveryRepository deliveryRepository;
	private final DriverRepository driverRepository;
	private final DeliveryStatusHistoryRepository statusHistoryRepository;

	@Override
	@Transactional
	public DeliveryAssignment assignDeliveryToDriver(Long deliveryId, Long driverId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

		// Validate driver availability
		if (driver.getStatus() != DriverStatus.READY_TO_ACCEPT_ORDERS) {
			throw new ResourceNotFoundException("Driver is not available");
		}

		// Check if there is any active assignment for this delivery and driver
		DeliveryAssignment existingAssignment = assignmentRepository
				.findByDeliveryIdAndDriverId(deliveryId, driverId).stream()
				.filter(a -> a
						.getStatus() == DeliveryAssignmentStatus.WAITING_FOR_DRIVER_ACCEPTANCE)
				.filter(a -> a.getExpiresAt() == null
						|| a.getExpiresAt().isAfter(LocalDateTime.now()))
				.findFirst().orElse(null);

		if (existingAssignment != null) {
			throw new BusinessException(
					"This delivery is already assigned to this driver and waiting for acceptance");
		}

		// Update delivery status
		delivery.setStatus(DeliveryStatus.WAITING_FOR_DRIVER_ACCEPTANCE);
		delivery.setDriver(driver);

		delivery = deliveryRepository.save(delivery);
		updateDeliveryStatus(delivery, DeliveryStatus.WAITING_FOR_DRIVER_ACCEPTANCE,
				"Assigned to driver: " + driver.getId());

		// Create assignment
		DeliveryAssignment assignment = DeliveryAssignment.builder().delivery(delivery)
				.driver(driver).assignedAt(LocalDateTime.now())
				.status(DeliveryAssignmentStatus.WAITING_FOR_DRIVER_ACCEPTANCE)
				.expiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_MINUTES))
				.build();

		return assignmentRepository.save(assignment);
	}

	@Override
	@Transactional
	public DeliveryAssignment acceptDelivery(Long deliveryId, Long driverId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

		DeliveryAssignment assignment = findActiveAssignment(deliveryId, driverId);

		if (assignment.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new ResourceNotFoundException("Assignment has expired");
		}
		delivery.setStatus(DeliveryStatus.DRIVER_ACCEPTED);
		driver.setStatus(DriverStatus.DELIVERING);
		// Update status history
		updateDeliveryStatus(delivery, DeliveryStatus.DRIVER_ACCEPTED,
				"Accepted by driver: " + driverId);

		delivery = deliveryRepository.save(delivery);
		driver = driverRepository.save(driver);

		assignment.setStatus(DeliveryAssignmentStatus.DRIVER_ACCEPTED);
		assignment.setRespondedAt(LocalDateTime.now());

		return assignmentRepository.save(assignment);
	}

	@Override
	public DeliveryAssignment rejectDelivery(Long deliveryId, Long driverId,
			String reason) {
		DeliveryAssignment assignment = findActiveAssignment(deliveryId, driverId);

		assignment.setStatus(DeliveryAssignmentStatus.DRIVER_REJECTED);
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

	@Override
	public DeliveryAssignment updateStatus(Long deliveryId, Long driverId,
			DeliveryStatus newStatus) {
		DeliveryAssignment assignment = findActiveAssignment(deliveryId, driverId);
		Delivery delivery = assignment.getDelivery();

		validateStatusTransition(delivery.getStatus(), newStatus);

		delivery.setStatus(newStatus);

		delivery = deliveryRepository.save(delivery);

		updateDeliveryStatus(delivery, newStatus,
				"Status updated by driver: " + driverId);

		if (newStatus == DeliveryStatus.DRIVER_ACCEPTED) {
			assignment.setStatus(DeliveryAssignmentStatus.DRIVER_ACCEPTED);
		} else if (newStatus == DeliveryStatus.CANCELLED) {
			assignment.setStatus(DeliveryAssignmentStatus.DRIVER_REJECTED);
		}

		return assignmentRepository.save(assignment);
	}

	private DeliveryAssignment findActiveAssignment(Long deliveryId, Long driverId) {
		List<DeliveryAssignment> assignments = assignmentRepository
				.findByDeliveryIdAndDriverId(deliveryId, driverId);

		if (assignments.isEmpty()) {
			return null;
		}

		return assignments.stream().filter(a -> a
				.getStatus() == DeliveryAssignmentStatus.WAITING_FOR_DRIVER_ACCEPTANCE)
				.filter(a -> a.getExpiresAt() == null
						|| a.getExpiresAt().isAfter(LocalDateTime.now()))
				.findFirst().orElseThrow(() -> new ResourceNotFoundException(
						"No active assignment found waiting for driver acceptance"));
	}

	private DeliveryStatusHistory updateDeliveryStatus(Delivery delivery,
			DeliveryStatus status, String note) {
		DeliveryStatusHistory statusHistory = DeliveryStatusHistory.builder()
				.delivery(delivery).status(status)
				.location(delivery.getDeliveryLocation()).note(note)
				.timestamp(LocalDateTime.now()).updatedBy("SYSTEM").build();

		return statusHistoryRepository.save(statusHistory);
	}

	private void validateStatusTransition(DeliveryStatus currentStatus,
			DeliveryStatus newStatus) {
		if (currentStatus == newStatus) {
			return; // Allow same status transition
		}

		boolean isValidTransition = switch (currentStatus) {
		case PENDING -> newStatus == DeliveryStatus.WAITING_FOR_DRIVER_ACCEPTANCE;
		case WAITING_FOR_DRIVER_ACCEPTANCE -> newStatus == DeliveryStatus.DRIVER_ACCEPTED
				|| newStatus == DeliveryStatus.CANCELLED;
		case DRIVER_ACCEPTED -> newStatus == DeliveryStatus.IN_TRANSIT
				|| newStatus == DeliveryStatus.CANCELLED;
		case IN_TRANSIT -> newStatus == DeliveryStatus.DELIVERED
				|| newStatus == DeliveryStatus.CANCELLED;
		case DELIVERED -> false; // Terminal state
		case CANCELLED -> false; // Terminal state
		};

		if (!isValidTransition) {
			throw new BusinessException(String.format(
					"Invalid status transition from %s to %s", currentStatus, newStatus));
		}
	}

	@Override
	@Transactional
	public DeliveryAssignment createDeliveryAssignment(
			DeliveryAssignmentCreateRequest request) {
		Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

		Driver driver = driverRepository.findById(request.getDriverId())
				.orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

		if (driver.getStatus() != DriverStatus.READY_TO_ACCEPT_ORDERS) {
			throw new ResourceNotFoundException("Driver is not available");
		}

		// Check if waiting for driver acceptance assignment already exists for
		// this
		// delivery and driver
		boolean existingAssignment = assignmentRepository
				.findByDeliveryIdAndDriverId(request.getDeliveryId(),
						request.getDriverId())
				.stream()
				.filter(a -> a
						.getStatus() == DeliveryAssignmentStatus.WAITING_FOR_DRIVER_ACCEPTANCE)
				.filter(a -> a.getExpiresAt() == null
						|| a.getExpiresAt().isAfter(LocalDateTime.now()))
				.findFirst().isPresent();

		if (existingAssignment) {
			throw new BusinessException(
					"Delivery assignment already exists for this delivery and driver");
		}

		delivery.setStatus(DeliveryStatus.WAITING_FOR_DRIVER_ACCEPTANCE);
		delivery = deliveryRepository.save(delivery);

		DeliveryAssignment assignment = DeliveryAssignment.builder().delivery(delivery)
				.driver(driver).warehouseIds(request.getWarehouseIds())
				.assignedAt(LocalDateTime.now())
				.status(DeliveryAssignmentStatus.WAITING_FOR_DRIVER_ACCEPTANCE)
				.rejectionReason(null).respondedAt(null)
				.expiresAt(LocalDateTime.now().plusMinutes(5)).build();

		return assignmentRepository.save(assignment);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DeliveryAssignment> getByDriver(Long driverId) {
		return assignmentRepository.findByDriverId(driverId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DeliveryAssignment> getByDelivery(Long deliveryId) {
		return assignmentRepository.findByDeliveryId(deliveryId);
	}

	@Override
	@Transactional
	public DeliveryAssignment driverStartDelivery(Long deliveryAssignmentId) {
		DeliveryAssignment assignment = assignmentRepository
				.findById(deliveryAssignmentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Delivery assignment not found"));

		Delivery delivery = assignment.getDelivery();

		if (delivery.getStatus() != DeliveryStatus.DRIVER_ACCEPTED) {
			throw new BusinessException("Delivery is not accepted by driver");
		}

		delivery.setStatus(DeliveryStatus.IN_TRANSIT);
		delivery = deliveryRepository.save(delivery);

		assignment.setStatus(DeliveryAssignmentStatus.IN_PROGRESS);
		return assignmentRepository.save(assignment);
	}

	@Override
	@Transactional
	public DeliveryAssignment driverDeliverDelivery(Long deliveryAssignmentId) {
		DeliveryAssignment assignment = assignmentRepository
				.findById(deliveryAssignmentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Delivery assignment not found"));

		Delivery delivery = assignment.getDelivery();
		Driver driver = assignment.getDriver();

		if (delivery.getStatus() != DeliveryStatus.IN_TRANSIT) {
			throw new BusinessException("Delivery is not in transit");
		}

		delivery.setStatus(DeliveryStatus.DELIVERED);
		delivery = deliveryRepository.save(delivery);

		driver.setStatus(DriverStatus.READY_TO_ACCEPT_ORDERS);
		driver = driverRepository.save(driver);

		assignment.setStatus(DeliveryAssignmentStatus.DELIVERED);
		return assignmentRepository.save(assignment);
	}

	@Override
	@Transactional
	public DeliveryAssignment refreshAssignment(Long driverId) {
		List<DeliveryAssignment> assignments = assignmentRepository
				.findByDriverId(driverId);

		// First update expired assignments
		List<DeliveryAssignment> expiredAssignments = assignments.stream().filter(a -> a
				.getStatus() == DeliveryAssignmentStatus.WAITING_FOR_DRIVER_ACCEPTANCE)
				.filter(a -> a.getExpiresAt() != null
						&& a.getExpiresAt().isBefore(LocalDateTime.now()))
				.toList();

		// Update expired assignments in a batch
		if (!expiredAssignments.isEmpty()) {
			expiredAssignments.forEach(assignment -> {
				assignment.setStatus(DeliveryAssignmentStatus.EXPIRED);
			});
			assignmentRepository.saveAll(expiredAssignments);
		}

		// Refresh the assignments list after updating expired ones
		assignments = assignmentRepository.findByDriverId(driverId);

		// Return first non-expired assignment
		return assignments.stream()
				.filter(a -> a.getStatus() != DeliveryAssignmentStatus.EXPIRED)
				.findFirst().orElse(null);
	}
}
