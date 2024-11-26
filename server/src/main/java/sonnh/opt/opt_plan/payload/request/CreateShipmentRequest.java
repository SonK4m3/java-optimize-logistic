package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShipmentRequest {
	@NotNull(message = "Warehouse ID is required")
	private Long warehouseId;

	@NotNull(message = "Shipment date is required")
	private LocalDateTime shipmentDate;

	private String notes;

	@NotEmpty(message = "Shipment details cannot be empty")
	@Valid
	private List<ShipmentDetailRequest> details;
}