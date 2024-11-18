package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.ProductDTO;
import sonnh.opt.opt_plan.payload.request.ProductCreateRequest;
import java.util.List;

public interface ProductService {
	ProductDTO createProduct(ProductCreateRequest request);

	ProductDTO getProductById(Long id);

	List<ProductDTO> getAllProducts();

	List<ProductDTO> getActiveProducts();
}