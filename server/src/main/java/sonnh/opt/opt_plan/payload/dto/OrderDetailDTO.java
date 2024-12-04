package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.OrderDetail;

/**
 * Data Transfer Object for OrderDetail entity.
 * 
 * @author Son Nguyen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
	private Long id;
	private Integer quantity;
	private Double price;
	private Double weight;
	private ProductDTO product;

	/**
	 * Maps an OrderDetail entity to an OrderDetailDTO.
	 * 
	 * @param orderDetail the OrderDetail entity to map.
	 * @return the mapped OrderDetailDTO.
	 */
	public static OrderDetailDTO fromEntity(OrderDetail orderDetail) {
		if (orderDetail == null)
			return null;
		return OrderDetailDTO.builder().id(orderDetail.getId())
				.quantity(orderDetail.getQuantity()).price(orderDetail.getPrice())
				.weight(orderDetail.getWeight())
				.product(ProductDTO.fromEntity(orderDetail.getProduct())).build();
	}
}
