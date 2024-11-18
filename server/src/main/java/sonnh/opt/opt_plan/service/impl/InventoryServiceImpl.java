package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Inventory;
import sonnh.opt.opt_plan.repository.InventoryRepository;
import sonnh.opt.opt_plan.service.InventoryService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
	private final InventoryRepository inventoryRepository;

	@Override
	public Inventory createInventory(Inventory inventory) {
		inventory.setLastUpdated(LocalDateTime.now());
		return inventoryRepository.save(inventory);
	}

	@Override
	public Inventory getInventoryById(Long id) {
		return inventoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
	}

	@Override
	public List<Inventory> getAllInventories() { return inventoryRepository.findAll(); }

}