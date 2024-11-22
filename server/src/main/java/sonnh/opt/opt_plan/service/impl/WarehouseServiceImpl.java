
package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sonnh.opt.opt_plan.exception.AuthenticationException;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.model.WarehouseReceipt;
import sonnh.opt.opt_plan.payload.dto.InventoryDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.request.InventoryUpdateRequest;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.ReceiptItemRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseUpdateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.repository.ProductRepository;
import sonnh.opt.opt_plan.repository.InventoryRepository;
import sonnh.opt.opt_plan.repository.WarehouseReceiptRepository;
import sonnh.opt.opt_plan.repository.ReceiptDetailRepository;
import sonnh.opt.opt_plan.service.WarehouseService;
import sonnh.opt.opt_plan.utils.SecurityUtils;
import sonnh.opt.opt_plan.model.ReceiptDetail;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.constant.enums.ReceiptType;
import sonnh.opt.opt_plan.constant.enums.ReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
	private final WarehouseRepository warehouseRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final InventoryRepository inventoryRepository;
	private final WarehouseReceiptRepository warehouseReceiptRepository;
	private final ReceiptDetailRepository receiptDetailRepository;
	private final SecurityUtils securityUtils;

	@Override
	@Transactional
	public WarehouseDTO createWarehouse(WarehouseCreateRequest request) {
		log.info("Creating new warehouse with code: {}", request.getCode());

		Warehouse warehouse = Warehouse.builder().code(request.getCode())
				.name(request.getName()).address(request.getAddress())
				.phone(request.getPhone()).email(request.getEmail())
				.area(request.getArea()).latitude(request.getLatitude())
				.type(request.getType()).longitude(request.getLongitude())
				.capacity(request.getTotalCapacity()).currentOccupancy(0).isActive(true)
				.build();

		warehouse = warehouseRepository.save(warehouse);
		log.info("Successfully created warehouse with ID: {}", warehouse.getId());
		return convertToDTO(warehouse);
	}

	@Override
	@Transactional
	public WarehouseDTO updateWarehouse(Long id, WarehouseUpdateRequest request) {
		log.info("Updating warehouse with ID: {}", id);

		Warehouse warehouse = getWarehouseOrThrow(id);

		updateWarehouseFields(warehouse, request);
		warehouse.setUpdatedAt(LocalDateTime.now());

		warehouse = warehouseRepository.save(warehouse);
		log.info("Successfully updated warehouse with ID: {}", id);
		return convertToDTO(warehouse);
	}

	@Override
	public WarehouseDTO getWarehouseById(Long id) {
		return convertToDTO(getWarehouseOrThrow(id));
	}

	@Override
	public WarehouseDTO getWarehouseByCode(String code) {
		return warehouseRepository.findAll().stream()
				.filter(w -> w.getCode().equals(code)).findFirst().map(this::convertToDTO)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Warehouse not found with code: " + code));
	}

	@Override
	public PageResponse<WarehouseDTO> getAllWarehouses(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Warehouse> warehouses = warehouseRepository.findAll(pageable);
		return PageResponse.<WarehouseDTO> builder()
				.docs(warehouses.stream().map(this::convertToDTO).toList())
				.totalDocs(warehouses.getTotalElements()).limit(size).page(page)
				.totalPages(warehouses.getTotalPages()).hasNextPage(warehouses.hasNext())
				.hasPrevPage(warehouses.hasPrevious()).build();
	}

	@Override
	public List<WarehouseDTO> getActiveWarehouses() {
		return warehouseRepository.findByIsActiveTrue().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<WarehouseDTO> getNearbyWarehouses(Double latitude, Double longitude,
			Double radius) {
		log.info("Finding warehouses near lat: {}, long: {} within {}km", latitude,
				longitude, radius);
		return warehouseRepository.findNearbyWarehouses(latitude, longitude, radius)
				.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<WarehouseDTO> getAvailableWarehouses(Integer requiredCapacity) {
		return warehouseRepository.findByIsActiveTrue().stream().filter(
				w -> (w.getCapacity() - w.getCurrentOccupancy()) >= requiredCapacity)
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public WarehouseDTO updateOccupancy(Long id, Integer newOccupancy) {
		log.info("Updating occupancy for warehouse ID: {} to {}", id, newOccupancy);

		Warehouse warehouse = getWarehouseOrThrow(id);
		validateOccupancy(warehouse, newOccupancy);

		warehouse.setCurrentOccupancy(newOccupancy);
		warehouse.setUpdatedAt(LocalDateTime.now());

		warehouse = warehouseRepository.save(warehouse);
		log.info("Successfully updated occupancy for warehouse ID: {}", id);
		return convertToDTO(warehouse);
	}

	@Override
	@Transactional
	public WarehouseDTO updateActiveStatus(Long id, Boolean isActive) {
		log.info("Updating active status for warehouse ID: {} to {}", id, isActive);

		Warehouse warehouse = getWarehouseOrThrow(id);
		warehouse.setIsActive(isActive);
		warehouse.setUpdatedAt(LocalDateTime.now());

		warehouse = warehouseRepository.save(warehouse);
		log.info("Successfully updated active status for warehouse ID: {}", id);
		return convertToDTO(warehouse);
	}

	@Override
	@Transactional
	public WarehouseDTO updateManager(Long warehouseId, Long managerId) {
		log.info("Updating manager for warehouse ID: {} to manager ID: {}", warehouseId,
				managerId);

		Warehouse warehouse = getWarehouseOrThrow(warehouseId);
		User manager = getUserOrThrow(managerId);

		warehouse.setManager(manager);
		warehouse.setUpdatedAt(LocalDateTime.now());

		warehouse = warehouseRepository.save(warehouse);
		log.info("Successfully updated manager for warehouse ID: {}", warehouseId);
		return convertToDTO(warehouse);
	}

	@Override
	public List<WarehouseDTO> getWarehousesByManager(Long managerId) {
		return warehouseRepository.findAll().stream().filter(
				w -> w.getManager() != null && w.getManager().getId().equals(managerId))
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<WarehouseDTO> getHighUtilizationWarehouses(Double threshold) {
		return warehouseRepository.findAll().stream()
				.filter(w -> calculateUtilizationRate(w.getId()) >= threshold)
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Double calculateUtilizationRate(Long id) {
		Warehouse warehouse = getWarehouseOrThrow(id);
		return warehouse.getCapacity() == 0 ? 0.0
				: (double) warehouse.getCurrentOccupancy() / warehouse.getCapacity()
						* 100;
	}

	@Override
	@Transactional
	public void deleteWarehouse(Long id) {
		log.info("Attempting to delete warehouse with ID: {}", id);

		Warehouse warehouse = getWarehouseOrThrow(id);
		if (warehouse.getCurrentOccupancy() > 0) {
			throw new IllegalStateException(
					"Cannot delete warehouse with existing inventory");
		}

		warehouseRepository.delete(warehouse);
		log.info("Successfully deleted warehouse with ID: {}", id);
	}

	@Override
	public List<WarehouseDTO> getLowCapacityWarehouses(Double threshold) {
		return warehouseRepository.findAll().stream()
				.filter(w -> calculateUtilizationRate(w.getId()) <= threshold)
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Integer getTotalAvailableCapacity() {
		return warehouseRepository.findByIsActiveTrue().stream()
				.mapToInt(w -> w.getCapacity() - w.getCurrentOccupancy()).sum();
	}

	@Override
	public Boolean hasAvailableCapacity(Long warehouseId, Integer requiredCapacity) {
		Warehouse warehouse = getWarehouseOrThrow(warehouseId);
		return (warehouse.getCapacity()
				- warehouse.getCurrentOccupancy()) >= requiredCapacity;
	}

	private Warehouse getWarehouseOrThrow(Long id) {
		return warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Warehouse not found with id: " + id));
	}

	private User getUserOrThrow(Long id) {
		return userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	private void validateOccupancy(Warehouse warehouse, Integer newOccupancy) {
		if (newOccupancy > warehouse.getCapacity()) {
			throw new IllegalArgumentException(
					"New occupancy exceeds warehouse capacity");
		}
	}

	private void updateWarehouseFields(Warehouse warehouse,
			WarehouseUpdateRequest request) {
		Optional.ofNullable(request.getCode()).ifPresent(warehouse::setCode);
		Optional.ofNullable(request.getName()).ifPresent(warehouse::setName);
		Optional.ofNullable(request.getAddress()).ifPresent(warehouse::setAddress);
		Optional.ofNullable(request.getPhone()).ifPresent(warehouse::setPhone);
		Optional.ofNullable(request.getEmail()).ifPresent(warehouse::setEmail);
		Optional.ofNullable(request.getLatitude()).ifPresent(warehouse::setLatitude);
		Optional.ofNullable(request.getLongitude()).ifPresent(warehouse::setLongitude);
	}

	private WarehouseDTO convertToDTO(Warehouse warehouse) {
		return WarehouseDTO.builder().id(warehouse.getId()).code(warehouse.getCode())
				.name(warehouse.getName()).address(warehouse.getAddress())
				.phone(warehouse.getPhone()).email(warehouse.getEmail())
				.latitude(warehouse.getLatitude()).longitude(warehouse.getLongitude())
				.totalCapacity(warehouse.getCapacity())
				.currentOccupancy(warehouse.getCurrentOccupancy())
				.isActive(warehouse.getIsActive()).createdAt(warehouse.getCreatedAt())
				.updatedAt(warehouse.getUpdatedAt()).build();
	}

	@Override
	@Transactional
	public WarehouseReceiptDTO createReceipt(Long warehouseId,
			ReceiptCreateRequest request) {
		log.info("Creating inbound receipt for warehouse ID: {}", warehouseId);

		// Validate user authentication
		User currentUser = securityUtils.getCurrentUser()
				.orElseThrow(() -> new AuthenticationException("User not authenticated"));

		// Get warehouse
		Warehouse warehouse = getWarehouseOrThrow(warehouseId);

		// Create warehouse receipt
		final WarehouseReceipt warehouseReceipt = createWarehouseReceipt(warehouse,
				currentUser, request);

		// Create and save receipt details
		List<ReceiptDetail> savedReceiptDetails = createAndSaveReceiptDetails(request,
				warehouseReceipt);

		// Update warehouse receipt with details
		warehouseReceipt.setReceiptDetails(savedReceiptDetails);
		WarehouseReceipt savedReceipt = warehouseReceiptRepository.save(warehouseReceipt);

		log.info("Successfully created inbound receipt with ID: {}",
				savedReceipt.getId());
		return WarehouseReceiptDTO.fromEntity(savedReceipt);
	}

	private WarehouseReceipt createWarehouseReceipt(Warehouse warehouse, User user,
			ReceiptCreateRequest request) {
		WarehouseReceipt receipt = WarehouseReceipt.builder().warehouse(warehouse)
				.code(WarehouseReceipt.generateCode()).type(request.getType())
				.receiptDate(LocalDateTime.now()).status(ReceiptStatus.DRAFT)
				.receiptDetails(new ArrayList<>()).createdBy(user).confirmedBy(null)
				.confirmedAt(null).notes(request.getNotes())
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
		return warehouseReceiptRepository.save(receipt);
	}

	private List<ReceiptDetail> createAndSaveReceiptDetails(ReceiptCreateRequest request,
			WarehouseReceipt receipt) {
		List<ReceiptDetail> receiptDetails = request.getItems().stream()
				.map(itemRequest -> {
					Product product = productRepository
							.findById(itemRequest.getProductId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Product not found with id: "
											+ itemRequest.getProductId()));

					ReceiptDetail detail = ReceiptDetail.builder().product(product)
							.quantity(itemRequest.getQuantity())
							.note(itemRequest.getNote()).warehouseReceipt(receipt)
							.build();

					return detail;
				}).collect(Collectors.toList());

		return receiptDetailRepository.saveAll(receiptDetails);
	}

	@Override
	public WarehouseReceiptDTO confirmReceipt(Long receiptId) {
		User currentUser = securityUtils.getCurrentUser()
				.orElseThrow(() -> new AuthenticationException("User not authenticated"));

		WarehouseReceipt warehouseReceipt = warehouseReceiptRepository.findById(receiptId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Warehouse receipt not found with id: " + receiptId));
		warehouseReceipt.setStatus(ReceiptStatus.APPROVED);
		warehouseReceipt.setConfirmedBy(currentUser);
		warehouseReceipt.setConfirmedAt(LocalDateTime.now());
		warehouseReceipt.setUpdatedAt(LocalDateTime.now());
		warehouseReceipt = warehouseReceiptRepository.save(warehouseReceipt);
		return WarehouseReceiptDTO.fromEntity(warehouseReceipt);
	}

	@Override
	public List<InventoryDTO> updateInventory(Long warehouseId,
			List<InventoryUpdateRequest> updates) {
		Warehouse warehouse = getWarehouseOrThrow(warehouseId);
		List<InventoryDTO> updatedInventories = new ArrayList<>();

		for (InventoryUpdateRequest update : updates) {
			Inventory inventory = inventoryRepository
					.findByWarehouseAndProduct(warehouse, update.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Inventory not found for product id: "
									+ update.getProductId()));
			inventory.setQuantity(update.getQuantity());
			inventory.setLastUpdated(LocalDateTime.now());
			inventoryRepository.save(inventory);
			updatedInventories.add(InventoryDTO.fromEntity(inventory));
		}

		return updatedInventories;
	}

	@Override
	public PageResponse<WarehouseReceiptDTO> getAllReceipts(Long warehouseId, int page,
			int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<WarehouseReceipt> receipts;
		if (warehouseId != null) {
			receipts = warehouseReceiptRepository.findByWarehouseIdAndType(warehouseId,
					ReceiptType.INBOUND, pageable);
		} else {
			receipts = warehouseReceiptRepository.findAll(pageable);
		}
		return PageResponse.<WarehouseReceiptDTO> builder()
				.docs(receipts.stream().map(WarehouseReceiptDTO::fromEntity).toList())
				.totalDocs(receipts.getTotalElements()).limit(size).page(page)
				.totalPages(receipts.getTotalPages()).hasNextPage(receipts.hasNext())
				.hasPrevPage(receipts.hasPrevious()).build();
	}

	@Override
	public WarehouseReceiptDTO rejectReceipt(Long receiptId) {
		WarehouseReceipt warehouseReceipt = warehouseReceiptRepository.findById(receiptId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Warehouse receipt not found with id: " + receiptId));
		warehouseReceipt.setStatus(ReceiptStatus.CANCELLED);
		warehouseReceipt.setUpdatedAt(LocalDateTime.now());
		warehouseReceipt.setConfirmedBy(null);
		warehouseReceipt.setConfirmedAt(null);
		warehouseReceipt = warehouseReceiptRepository.save(warehouseReceipt);
		return WarehouseReceiptDTO.fromEntity(warehouseReceipt);
	}
}