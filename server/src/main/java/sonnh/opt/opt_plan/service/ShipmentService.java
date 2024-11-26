package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sonnh.opt.opt_plan.constant.enums.ShipmentStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Shipment;
import sonnh.opt.opt_plan.model.ShipmentDetail;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.repository.ShipmentRepository;
import sonnh.opt.opt_plan.payload.request.CreateShipmentRequest;

import java.time.LocalDateTime;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
@RequiredArgsConstructor
public class ShipmentService {
	private final ShipmentRepository shipmentRepository;
	private final InventoryService inventoryService;
	private final UserService userService;
	private final WarehouseService warehouseService;
	private final ProductService productService;

	/**
	 * Create new shipment
	 * 
	 * @param request CreateShipmentRequest containing shipment details
	 * @param userId  ID of the user creating the shipment
	 * @return Created Shipment
	 */
	public Shipment createShipment(CreateShipmentRequest request, Long userId) {
		User creator = userService.getUserById(userId);
		Warehouse warehouse = warehouseService.getWarehouseById(request.getWarehouseId());

		Shipment shipment = Shipment.builder().code(Shipment.generateCode())
				.warehouse(warehouse).shipmentDate(request.getShipmentDate())
				.notes(request.getNotes()).createdBy(creator)
				.status(ShipmentStatus.PENDING).build();

		request.getDetails().forEach(detail -> {
			ShipmentDetail shipmentDetail = ShipmentDetail.builder()
					.product(productService.getProductById(detail.getProductId()))
					.quantity(detail.getQuantity()).note(detail.getNote()).build();
			shipment.addDetail(shipmentDetail);
		});

		return shipmentRepository.save(shipment);
	}

	/**
	 * Confirm shipment and update inventory
	 * 
	 * @param shipmentId ID of shipment to confirm
	 * @param userId     ID of user confirming the shipment
	 * @return Updated Shipment
	 * @throws BadRequestException
	 */
	@Transactional
	public Shipment confirmShipment(Long shipmentId, Long userId)
			throws BadRequestException {
		Shipment shipment = getShipmentById(shipmentId);

		if (shipment.getStatus() != ShipmentStatus.PENDING) {
			throw new BadRequestException(
					"Shipment cannot be confirmed in current status");
		}

		User confirmer = userService.getUserById(userId);

		// Update inventory for each product
		shipment.getDetails().forEach(detail -> {
			inventoryService.decreaseStock(shipment.getWarehouse().getId(),
					detail.getProduct().getId(), detail.getQuantity());
		});

		shipment.setStatus(ShipmentStatus.CONFIRMED);
		shipment.setConfirmedBy(confirmer);
		shipment.setConfirmedAt(LocalDateTime.now());

		return shipmentRepository.save(shipment);
	}

	/**
	 * Cancel shipment
	 * 
	 * @param shipmentId ID of shipment to cancel
	 * @return Updated Shipment
	 * @throws BadRequestException
	 */
	public Shipment cancelShipment(Long shipmentId, Long userId)
			throws BadRequestException {
		Shipment shipment = getShipmentById(shipmentId);

		if (shipment.getStatus() != ShipmentStatus.PENDING) {
			throw new BadRequestException(
					"Shipment cannot be cancelled in current status");
		}

		shipment.setStatus(ShipmentStatus.CANCELLED);
		return shipmentRepository.save(shipment);
	}

	public Shipment getShipmentById(Long id) {
		return shipmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
	}

	public Page<Shipment> getShipments(Pageable pageable) {
		return shipmentRepository.findAll(pageable);
	}
}