package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.dto.ProductDTO;
import sonnh.opt.opt_plan.payload.request.ProductCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;

import java.util.List;

public interface ProductService {
	ProductDTO createProduct(ProductCreateRequest request);

	ProductDTO getProductById(Long id);

	PageResponse<ProductDTO> getAllProducts(String query, int page, int size);

	List<ProductDTO> getActiveProducts();
}