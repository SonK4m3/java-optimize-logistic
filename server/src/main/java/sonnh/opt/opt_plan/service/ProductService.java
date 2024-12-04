package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.request.ProductCreateRequest;
import sonnh.opt.opt_plan.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
	Product createProduct(ProductCreateRequest request);

	Product getProductById(Long id);

	Page<Product> getAllProducts(String query, int page, int size);
}