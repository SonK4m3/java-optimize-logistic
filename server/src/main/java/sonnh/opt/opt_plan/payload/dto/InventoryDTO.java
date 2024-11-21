package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
	private Long id;
	private Long productId;
	private String productCode;
	private String productName;
	private Long warehouseId;
	private Integer quantity;
	private String unit;
	private LocalDateTime lastUpdated;
}