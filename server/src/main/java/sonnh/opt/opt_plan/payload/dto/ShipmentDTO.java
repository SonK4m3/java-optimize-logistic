package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.ShipmentStatus;
import sonnh.opt.opt_plan.model.Shipment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ShipmentDTO {
	private Long id;
	private String code;
	private ShipmentStatus status;
	private LocalDateTime shipmentDate;
	private WarehouseDTO warehouse;
	private UserDTO createdBy;
	private UserDTO confirmedBy;
	private LocalDateTime confirmedAt;
	private String notes;
	private List<ShipmentDetailDTO> details;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ShipmentDTO fromEntity(Shipment shipment) {
		if (shipment == null) {
			return null;
		}

		return ShipmentDTO.builder().id(shipment.getId()).code(shipment.getCode())
				.status(shipment.getStatus()).shipmentDate(shipment.getShipmentDate())
				.warehouse(WarehouseDTO.fromEntity(shipment.getWarehouse()))
				.createdBy(UserDTO.fromEntity(shipment.getCreatedBy()))
				.confirmedBy(UserDTO.fromEntity(shipment.getConfirmedBy()))
				.confirmedAt(shipment.getConfirmedAt()).notes(shipment.getNotes())
				.details(shipment.getDetails().stream().map(ShipmentDetailDTO::fromEntity)
						.collect(Collectors.toList()))
				.createdAt(shipment.getCreatedAt()).updatedAt(shipment.getUpdatedAt())
				.build();
	}
}