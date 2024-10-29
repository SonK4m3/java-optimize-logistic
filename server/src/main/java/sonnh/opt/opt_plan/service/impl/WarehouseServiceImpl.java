package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Warehouse;
import sonnh.opt.opt_plan.repository.WarehouseRepository;
import sonnh.opt.opt_plan.service.WarehouseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
	private final WarehouseRepository warehouseRepository;

	@Override
	public Warehouse createWarehouse(Warehouse warehouse) {
		if (warehouseRepository.existsByCode(warehouse.getCode())) {
			throw new IllegalArgumentException("Warehouse code already exists");
		}
		warehouse.setIsActive(true);
		return warehouseRepository.save(warehouse);
	}

	@Override
	public Warehouse updateWarehouse(Long id, Warehouse warehouse) {
		Warehouse existingWarehouse = getWarehouseById(id);
		existingWarehouse.setName(warehouse.getName());
		existingWarehouse.setAddress(warehouse.getAddress());
		existingWarehouse.setPhone(warehouse.getPhone());
		existingWarehouse.setCapacity(warehouse.getCapacity());
		existingWarehouse.setDescription(warehouse.getDescription());
		existingWarehouse.setIsActive(warehouse.getIsActive());
		return warehouseRepository.save(existingWarehouse);
	}

	@Override
	public Warehouse getWarehouseById(Long id) {
		return warehouseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id));
	}

	@Override
	public Warehouse getWarehouseByCode(String code) {
		return warehouseRepository.findByCode(code)
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse", "code", code));
	}

	@Override
	public List<Warehouse> getAllWarehouses() {
		return warehouseRepository.findAll();
	}

	@Override
	public List<Warehouse> getActiveWarehouses() {
		return warehouseRepository.findByIsActive(true);
	}

	@Override
	public void deleteWarehouse(Long id) {
		Warehouse warehouse = getWarehouseById(id);
		warehouse.setIsActive(false);
		warehouseRepository.save(warehouse);
	}

	@Override
	public boolean existsByCode(String code) {
		return warehouseRepository.existsByCode(code);
	}
}