package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReceiptDTO {
	private Long id;
	private String receiptNumber;
	private LocalDateTime createdAt;
	private String status;
	private String type; // INBOUND or OUTBOUND
	private Long warehouseId;
	private List<ReceiptItemDTO> items;
	private String notes;
	private String createdBy;
	private String confirmedBy;
	private LocalDateTime confirmedAt;
}