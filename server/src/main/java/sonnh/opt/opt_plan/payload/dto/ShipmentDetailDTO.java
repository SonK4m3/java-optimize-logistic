package sonnh.opt.opt_plan.payload.dto;

import sonnh.opt.opt_plan.model.ShipmentDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipmentDetailDTO {
	private Long id;
	private ProductDTO product;
	private Integer quantity;
	private String note;

	public static ShipmentDetailDTO fromEntity(ShipmentDetail detail) {
		if (detail == null) {
			return null;
		}

		return ShipmentDetailDTO.builder().id(detail.getId())
				.product(ProductDTO.fromEntity(detail.getProduct()))
				.quantity(detail.getQuantity()).note(detail.getNote()).build();
	}
}