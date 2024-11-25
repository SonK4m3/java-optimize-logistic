package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.InventoryDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseSpaceDTO;
import sonnh.opt.opt_plan.payload.request.InventoryUpdateRequest;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import java.util.List;
import org.apache.coyote.BadRequestException;

public interface WarehouseService {
	WarehouseDTO createWarehouse(WarehouseCreateRequest request);

	WarehouseDTO getWarehouseById(Long id);

	PageResponse<WarehouseDTO> getAllWarehouses(int page, int size);

	List<WarehouseDTO> getAvailableWarehouses(Integer requiredCapacity);

	WarehouseDTO updateManager(Long warehouseId, Long managerId);

	List<WarehouseDTO> getWarehousesByManager(Long managerId);

	WarehouseReceiptDTO confirmReceipt(Long receiptId) throws BadRequestException;

	List<InventoryDTO> updateInventory(Long warehouseId,
			List<InventoryUpdateRequest> updates);

	WarehouseReceiptDTO createReceipt(Long warehouseId, ReceiptCreateRequest request);

	PageResponse<WarehouseReceiptDTO> getAllReceipts(Long warehouseId, int page,
			int size);

	WarehouseReceiptDTO rejectReceipt(Long receiptId);

	/**
	 * Check warehouse space availability
	 * 
	 * @param warehouseId ID of the warehouse to check
	 * @return DTO containing space utilization details
	 */
	WarehouseSpaceDTO checkWarehouseSpace(Long warehouseId);
}