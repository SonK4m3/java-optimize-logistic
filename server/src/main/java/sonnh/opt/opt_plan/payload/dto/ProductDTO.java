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
	private String unit;
	private Double price;
	private Double weight;
	private String dimensions;
	private Integer minStockLevel;
	private Integer maxStockLevel;
	private Integer reorderPoint;
	private StorageCondition storageCondition;
	private String imageUrl;
	private CategoryDTO category;
	private SupplierDTO supplier;

	public static ProductDTO fromEntity(Product product) {
		if (product == null)
			return null;
		return ProductDTO.builder().id(product.getId()).code(product.getCode())
				.name(product.getName()).unit(product.getUnit()).price(product.getPrice())
				.weight(product.getWeight()).dimensions(product.getDimensions())
				.minStockLevel(product.getMinStockLevel())
				.maxStockLevel(product.getMaxStockLevel())
				.reorderPoint(product.getReorderPoint())
				.storageCondition(product.getStorageCondition())
				.imageUrl(product.getImageUrl())
				.category(CategoryDTO.fromEntity(product.getCategory()))
				.supplier(SupplierDTO.fromEntity(product.getSupplier())).build();
	}
}