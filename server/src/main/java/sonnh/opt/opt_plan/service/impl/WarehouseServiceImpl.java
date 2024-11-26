
package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import sonnh.opt.opt_plan.exception.AuthenticationException;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.model.WarehouseReceipt;
import sonnh.opt.opt_plan.model.ReceiptDetail;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.model.StorageArea;
import sonnh.opt.opt_plan.model.StorageLocation;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.model.Location;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.payload.dto.InventoryDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseSpaceDTO;
import sonnh.opt.opt_plan.payload.request.InventoryUpdateRequest;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.repository.ProductRepository;
import sonnh.opt.opt_plan.repository.InventoryRepository;
import sonnh.opt.opt_plan.repository.WarehouseReceiptRepository;
import sonnh.opt.opt_plan.repository.ReceiptDetailRepository;
import sonnh.opt.opt_plan.repository.StaffRepository;
import sonnh.opt.opt_plan.repository.LocationRepository;
import sonnh.opt.opt_plan.repository.StorageLocationRepository;
import sonnh.opt.opt_plan.service.WarehouseService;
import sonnh.opt.opt_plan.utils.SecurityUtils;
import sonnh.opt.opt_plan.constant.enums.WarehouseStatus;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;
import sonnh.opt.opt_plan.constant.enums.InventoryStatus;
import sonnh.opt.opt_plan.constant.enums.ReceiptStatus;
import sonnh.opt.opt_plan.constant.enums.ReceiptType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
	private final WarehouseRepository warehouseRepository;
	private final StaffRepository staffRepository;
	private final ProductRepository productRepository;
	private final InventoryRepository inventoryRepository;
	private final WarehouseReceiptRepository warehouseReceiptRepository;
	private final ReceiptDetailRepository receiptDetailRepository;
	private final SecurityUtils securityUtils;
	private final LocationRepository locationRepository;
	private final StorageLocationRepository storageLocationRepository;

	@Override
	@Transactional
	public WarehouseDTO createWarehouse(WarehouseCreateRequest request) {
		Staff manager = staffRepository.findById(request.getManagerId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Manager not found with id: " + request.getManagerId()));

		Location location = Location.builder().address(request.getAddress())
				.latitude(request.getLatitude()).longitude(request.getLongitude())
				.build();

		location = locationRepository.save(location);

		Warehouse warehouse = Warehouse.builder().code(Warehouse.generateCode())
				.name(request.getName()).location(location)
				.totalArea(request.getTotalArea())
				.totalCapacity(request.getTotalCapacity()).type(request.getType())
				.manager(manager).build();

		warehouse = warehouseRepository.save(warehouse);
		return WarehouseDTO.fromEntity(warehouse);
	}

	@Override
	public Warehouse getWarehouseById(Long id) { return getWarehouseOrThrow(id); }

	@Override
	public PageResponse<WarehouseDTO> getAllWarehouses(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Warehouse> warehouses = warehouseRepository.findAll(pageable);
		return PageResponse.<WarehouseDTO> builder()
				.docs(warehouses.stream().map(WarehouseDTO::fromEntity).toList())
				.totalDocs(warehouses.getTotalElements()).limit(size).page(page)
				.totalPages(warehouses.getTotalPages()).hasNextPage(warehouses.hasNext())
				.hasPrevPage(warehouses.hasPrevious()).build();
	}

	@Override
	public List<WarehouseDTO> getAvailableWarehouses(Integer requiredCapacity) {
		List<Warehouse> warehouses = warehouseRepository
				.findByStatusAndType(WarehouseStatus.ACTIVE, WarehouseType.NORMAL);

		return warehouses.stream().filter(warehouse -> {
			List<StorageArea> storageAreas = warehouse.getStorageAreas();
			List<StorageLocation> storageLocations = storageAreas.stream()
					.flatMap(area -> area.getStorageLocations().stream()).toList();

			Integer currentOccupancy = inventoryRepository
					.findByStorageLocationIn(storageLocations).stream()
					.mapToInt(Inventory::getQuantity).sum();

			return (warehouse.getTotalCapacity() - currentOccupancy) >= requiredCapacity;
		}).map(WarehouseDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public WarehouseDTO updateManager(Long warehouseId, Long managerId) {
		Warehouse warehouse = getWarehouseOrThrow(warehouseId);
		Staff manager = staffRepository.findById(managerId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Manager not found with id: " + managerId));

		warehouse.setManager(manager);
		warehouse.setUpdatedAt(LocalDateTime.now());

		warehouse = warehouseRepository.save(warehouse);
		return WarehouseDTO.fromEntity(warehouse);
	}

	@Override
	public List<WarehouseDTO> getWarehousesByManager(Long managerId) {
		return warehouseRepository.findAll().stream().filter(
				w -> w.getManager() != null && w.getManager().getId().equals(managerId))
				.map(WarehouseDTO::fromEntity).collect(Collectors.toList());
	}

	private Warehouse getWarehouseOrThrow(Long id) {
		return warehouseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Warehouse not found with id: " + id));
	}

	@Override
	@Transactional
	public WarehouseReceiptDTO createReceipt(Long warehouseId,
			ReceiptCreateRequest request) {
		User currentUser = securityUtils.getCurrentUser()
				.orElseThrow(() -> new AuthenticationException("User not authenticated"));

		StorageLocation storageLocation = storageLocationRepository
				.findById(request.getStorageLocationId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Storage location not found with id: "
								+ request.getStorageLocationId()));

		final WarehouseReceipt warehouseReceipt = createWarehouseReceipt(storageLocation,
				currentUser, request);

		List<ReceiptDetail> savedReceiptDetails = createAndSaveReceiptDetails(request,
				warehouseReceipt);

		warehouseReceipt.setReceiptDetails(savedReceiptDetails);
		WarehouseReceipt savedReceipt = warehouseReceiptRepository.save(warehouseReceipt);

		return WarehouseReceiptDTO.fromEntity(savedReceipt);
	}

	private WarehouseReceipt createWarehouseReceipt(StorageLocation storageLocation,
			User user, ReceiptCreateRequest request) {
		WarehouseReceipt receipt = WarehouseReceipt.builder()
				.storageLocation(storageLocation).code(WarehouseReceipt.generateCode())
				.type(request.getType()).receiptDate(LocalDateTime.now())
				.status(ReceiptStatus.DRAFT).receiptDetails(new ArrayList<>())
				.createdBy(user).confirmedBy(null).confirmedAt(null)
				.notes(request.getNotes()).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();
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
	@Transactional
	public WarehouseReceiptDTO confirmReceipt(Long receiptId) throws BadRequestException {
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

		Warehouse warehouse = warehouseReceipt.getStorageLocation().getStorageArea()
				.getWarehouse();

		for (ReceiptDetail detail : warehouseReceipt.getReceiptDetails()) {
			Inventory inventory = inventoryRepository.findByStorageLocationAndProduct(
					warehouseReceipt.getStorageLocation().getId(),
					detail.getProduct().getId()).orElse(null);

			if (inventory == null) {
				inventory = Inventory.builder()
						.storageLocation(warehouseReceipt.getStorageLocation())
						.product(detail.getProduct()).quantity(detail.getQuantity())
						.status(InventoryStatus.ACTIVE).location("A1").minQuantity(0)
						.maxQuantity(100).lastCheckedAt(LocalDateTime.now())
						.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
						.expiryDate(LocalDateTime.now().plusYears(1)).build();
			} else {
				if (warehouseReceipt.getType() == ReceiptType.INBOUND) {
					if (getAvailableCapacity(warehouse) < detail.getQuantity()) {
						warehouseReceipt.setStatus(ReceiptStatus.CANCELLED);
						warehouseReceiptRepository.save(warehouseReceipt);
						throw new BadRequestException(
								"Warehouse does not have enough capacity");
					}
					inventory.setQuantity(inventory.getQuantity() + detail.getQuantity());
				} else {
					if (inventory.getQuantity() - detail.getQuantity() < 0) {
						warehouseReceipt.setStatus(ReceiptStatus.CANCELLED);
						warehouseReceiptRepository.save(warehouseReceipt);
						throw new BadRequestException("Inventory quantity is not enough");
					}
					inventory.setQuantity(inventory.getQuantity() - detail.getQuantity());
				}
			}

			inventory = inventoryRepository.save(inventory);
		}

		return WarehouseReceiptDTO.fromEntity(warehouseReceipt);
	}

	@Override
	public PageResponse<WarehouseReceiptDTO> getAllReceipts(Long storageLocationId,
			int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size,
				Sort.by("createdAt").descending());
		Page<WarehouseReceipt> receipts;
		if (storageLocationId != null) {
			receipts = warehouseReceiptRepository.findByStorageLocationIdAndType(
					storageLocationId, ReceiptType.INBOUND, pageable);
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

	@Override
	public WarehouseSpaceDTO checkWarehouseSpace(Long warehouseId) {
		Warehouse warehouse = getWarehouseOrThrow(warehouseId);

		List<StorageArea> storageAreas = warehouse.getStorageAreas();

		List<StorageLocation> storageLocations = storageAreas.stream()
				.flatMap(area -> area.getStorageLocations().stream()).toList();

		int usedCapacity = inventoryRepository.findByStorageLocationIn(storageLocations)
				.stream().mapToInt(Inventory::getQuantity).sum();

		int availableCapacity = warehouse.getTotalCapacity() - usedCapacity;

		double usedArea = warehouse.getStorageAreas().stream()
				.mapToDouble(StorageArea::getArea).sum();

		double availableArea = warehouse.getTotalArea() - usedArea;

		double spaceUtilization = (usedArea / warehouse.getTotalArea()) * 100;
		double capacityUtilization = ((double) usedCapacity
				/ warehouse.getTotalCapacity()) * 100;

		return WarehouseSpaceDTO.builder().warehouseId(warehouse.getId())
				.warehouseCode(warehouse.getCode()).totalArea(warehouse.getTotalArea())
				.usedArea(usedArea).availableArea(availableArea)
				.totalCapacity(warehouse.getTotalCapacity()).usedCapacity(usedCapacity)
				.availableCapacity(availableCapacity)
				.spaceUtilizationPercentage(spaceUtilization)
				.capacityUtilizationPercentage(capacityUtilization).build();
	}

	private Integer getAvailableCapacity(Warehouse warehouse) {
		List<StorageArea> storageAreas = warehouse.getStorageAreas();
		List<StorageLocation> storageLocations = storageAreas.stream()
				.flatMap(area -> area.getStorageLocations().stream()).toList();

		int usedCapacity = inventoryRepository.findByStorageLocationIn(storageLocations)
				.stream().mapToInt(Inventory::getQuantity).sum();

		return warehouse.getTotalCapacity() - usedCapacity;
	}

	private Double getAvailableArea(Warehouse warehouse) {
		List<StorageArea> storageAreas = warehouse.getStorageAreas();
		List<StorageLocation> storageLocations = storageAreas.stream()
				.flatMap(area -> area.getStorageLocations().stream()).toList();

		double usedArea = inventoryRepository.findByStorageLocationIn(storageLocations)
				.stream().mapToDouble(Inventory::getQuantity).sum();

		return warehouse.getTotalArea() - usedArea;
	}
}