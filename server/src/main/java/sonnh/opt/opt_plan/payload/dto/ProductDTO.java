package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.model.Product;

@Data
@Builder
public class ProductDTO {
	private Long id;
	private String name;
	private String sku;
	private Double weight;
	private String dimensions;
	private Boolean isFragile;

	public static ProductDTO fromEntity(Product product) {
		if (product == null)
			return null;
		return ProductDTO.builder().id(product.getId()).name(product.getName())
				.sku(product.getSku()).weight(product.getWeight())
				.dimensions(product.getDimensions()).isFragile(product.getIsFragile())
				.build();
	}
}