package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.WarehouseDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseReceiptDTO;
import sonnh.opt.opt_plan.payload.dto.WarehouseSpaceDTO;
import sonnh.opt.opt_plan.payload.request.ReceiptCreateRequest;
import sonnh.opt.opt_plan.payload.request.WarehouseCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import java.util.List;
import org.apache.coyote.BadRequestException;
import sonnh.opt.opt_plan.model.Warehouse;

public interface WarehouseService {
	WarehouseDTO createWarehouse(WarehouseCreateRequest request);

	Warehouse getWarehouseById(Long id);

	PageResponse<WarehouseDTO> getAllWarehouses(int page, int size);

	List<WarehouseDTO> getAvailableWarehouses(Integer requiredCapacity);

	WarehouseDTO updateManager(Long warehouseId, Long managerId);

	List<WarehouseDTO> getWarehousesByManager(Long managerId);

	WarehouseReceiptDTO confirmReceipt(Long receiptId) throws BadRequestException;

	WarehouseReceiptDTO createReceipt(Long warehouseId, ReceiptCreateRequest request);

	PageResponse<WarehouseReceiptDTO> getAllReceipts(Long storageLocationId, int page,
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