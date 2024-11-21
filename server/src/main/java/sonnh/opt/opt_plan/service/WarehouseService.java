
package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.InventoryDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.request.InventoryUpdateRequest;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseUpdateRequest;

import java.util.List;

public interface WarehouseService {
	WarehouseDTO createWarehouse(WarehouseCreateRequest request);

	WarehouseDTO updateWarehouse(Long id, WarehouseUpdateRequest request);

	WarehouseDTO getWarehouseById(Long id);

	WarehouseDTO getWarehouseByCode(String code);

	List<WarehouseDTO> getAllWarehouses();

	List<WarehouseDTO> getActiveWarehouses();

	List<WarehouseDTO> getNearbyWarehouses(Double latitude, Double longitude,
			Double radius);

	List<WarehouseDTO> getAvailableWarehouses(Integer requiredCapacity);

	WarehouseDTO updateOccupancy(Long id, Integer newOccupancy);

	WarehouseDTO updateActiveStatus(Long id, Boolean isActive);

	WarehouseDTO updateManager(Long warehouseId, Long managerId);

	List<WarehouseDTO> getWarehousesByManager(Long managerId);

	List<WarehouseDTO> getHighUtilizationWarehouses(Double threshold);

	Double calculateUtilizationRate(Long id);

	void deleteWarehouse(Long id);

	List<WarehouseDTO> getLowCapacityWarehouses(Double threshold);

	Integer getTotalAvailableCapacity();

	Boolean hasAvailableCapacity(Long warehouseId, Integer requiredCapacity);

	WarehouseReceiptDTO createInboundReceipt(Long warehouseId,
			ReceiptCreateRequest request);

	WarehouseReceiptDTO confirmInboundReceipt(Long receiptId);

	List<InventoryDTO> updateInventory(Long warehouseId,
			List<InventoryUpdateRequest> updates);

	WarehouseReceiptDTO createOutboundReceipt(Long warehouseId,
			ReceiptCreateRequest request);

	WarehouseReceiptDTO confirmOutboundReceipt(Long receiptId);
}