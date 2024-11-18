package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.constant.enums.StorageCondition;

@Data
@Builder
public class ProductDTO {
	private Long id;
	private String code;
	private String name;
	private String barcode;
	private String unit;
	private Double length;
	private Double width;
	private Double height;
	private Double weight;
	private StorageCondition storageCondition;

	public static ProductDTO fromEntity(Product product) {
		if (product == null)
			return null;
		return ProductDTO.builder().id(product.getId()).code(product.getCode())
				.name(product.getName()).barcode(product.getBarcode())
				.unit(product.getUnit()).length(product.getLength())
				.width(product.getWidth()).height(product.getHeight())
				.weight(product.getWeight())
				.storageCondition(product.getStorageCondition()).build();
	}
}