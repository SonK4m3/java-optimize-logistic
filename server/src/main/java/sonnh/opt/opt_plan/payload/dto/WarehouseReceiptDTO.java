package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.model.WarehouseReceipt;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReceiptDTO {
	private Long id;
	private String receiptNumber;
	private LocalDateTime createdAt;
	private String status;
	private String type;
	private Long warehouseId;
	private List<ReceiptItemDTO> items;
	private String notes;
	private String createdBy;
	private String confirmedBy;
	private LocalDateTime confirmedAt;

	public static WarehouseReceiptDTO fromEntity(WarehouseReceipt warehouseReceipt) {
		return WarehouseReceiptDTO.builder().id(warehouseReceipt.getId())
				.receiptNumber(warehouseReceipt.getCode())
				.createdAt(warehouseReceipt.getCreatedAt())
				.status(warehouseReceipt.getStatus().name())
				.type(warehouseReceipt.getType().name())
				.warehouseId(warehouseReceipt.getWarehouse().getId())
				.items(warehouseReceipt.getReceiptDetails().stream()
						.map(ReceiptItemDTO::fromEntity).collect(Collectors.toList()))
				.notes(warehouseReceipt.getNotes())
				.createdBy(warehouseReceipt.getCreatedBy() != null
						? warehouseReceipt.getCreatedBy().getUsername()
						: null)
				.confirmedBy(warehouseReceipt.getConfirmedBy() != null
						? warehouseReceipt.getConfirmedBy().getUsername()
						: null)
				.confirmedAt(warehouseReceipt.getConfirmedAt() != null
						? warehouseReceipt.getConfirmedAt()
						: null)
				.build();
	}
}