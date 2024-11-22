package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.constant.enums.StorageCondition;

import java.util.UUID;

@Data
@Builder
public class ProductDTO {
	private Long id;
	private String code;
	private String name;
	private String unit;
	private Double price;
	private StorageCondition storageCondition;

	public static ProductDTO fromEntity(Product product) {
		if (product == null)
			return null;
		return ProductDTO.builder().id(product.getId()).code(product.getCode())
				.name(product.getName()).unit(product.getUnit()).price(product.getPrice())
				.storageCondition(product.getStorageCondition()).build();
	}

	public static String generateCode(String name) {
		return name.substring(0, 3).toUpperCase() + "-"
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
}