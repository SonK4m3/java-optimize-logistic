package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.OrderProduct;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDTO {
	private Long id;
	private ProductDTO product;
	private Integer quantity;
	private Double unitPrice;

	public static OrderProductDTO fromEntity(OrderProduct orderProduct) {
		if (orderProduct == null)
			return null;
		return OrderProductDTO.builder().id(orderProduct.getId())
				.product(ProductDTO.fromEntity(orderProduct.getProduct()))
				.quantity(orderProduct.getQuantity())
				.unitPrice(orderProduct.getUnitPrice()).build();
	}
}