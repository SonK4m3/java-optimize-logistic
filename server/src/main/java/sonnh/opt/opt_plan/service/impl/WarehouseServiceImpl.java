
package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.payload.dto.InventoryDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.request.InventoryUpdateRequest;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseUpdateRequest;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.service.WarehouseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
	private final WarehouseRepository warehouseRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public WarehouseDTO createWarehouse(WarehouseCreateRequest request) {
		log.info("Creating new warehouse with code: {}", request.getCode());

		Warehouse warehouse = Warehouse.builder().code(request.getCode())
				.name(request.getName()).address(request.getAddress())
				.phone(request.getPhone()).email(request.getEmail())
				.area(request.getArea()).latitude(request.getLatitude())
				.longitude(request.getLongitude()).capacity(request.getTotalCapacity())
				.currentOccupancy(0).isActive(true).build();

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
	public List<WarehouseDTO> getAllWarehouses() {
		return warehouseRepository.findAll().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
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
	public WarehouseReceiptDTO createInboundReceipt(Long warehouseId,
			ReceiptCreateRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"Unimplemented method 'createInboundReceipt'");
	}

	@Override
	public WarehouseReceiptDTO confirmInboundReceipt(Long receiptId) { // TODO
																		// Auto-generated
																		// method
																		// stub
		throw new UnsupportedOperationException(
				"Unimplemented method 'confirmInboundReceipt'");
	}

	@Override
	public List<InventoryDTO> updateInventory(Long warehouseId,
			List<InventoryUpdateRequest> updates) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateInventory'");
	}

	@Override
	public WarehouseReceiptDTO createOutboundReceipt(Long warehouseId,
			ReceiptCreateRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"Unimplemented method 'createOutboundReceipt'");
	}

	@Override
	public WarehouseReceiptDTO confirmOutboundReceipt(Long receiptId) { // TODO
																		// Auto-generated
																		// method
																		// stub
		throw new UnsupportedOperationException(
				"Unimplemented method 'confirmOutboundReceipt'");
	}
}