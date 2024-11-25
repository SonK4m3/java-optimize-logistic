package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.model.Category;
import sonnh.opt.opt_plan.model.Supplier;
import sonnh.opt.opt_plan.payload.dto.ProductDTO;
import sonnh.opt.opt_plan.payload.request.ProductCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.repository.ProductRepository;
import sonnh.opt.opt_plan.repository.CategoryRepository;
import sonnh.opt.opt_plan.repository.SupplierRepository;
import sonnh.opt.opt_plan.service.ProductService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final SupplierRepository supplierRepository;

	@Override
	@Transactional
	public ProductDTO createProduct(ProductCreateRequest request) {
		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		Supplier supplier = supplierRepository.findById(request.getSupplierId())
				.orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

		Product product = Product.builder().code(Product.generateCode(request.getName()))
				.name(request.getName()).unit(request.getUnit()).price(request.getPrice())
				.weight(request.getWeight()).dimensions(request.getDimensions())
				.minStockLevel(request.getMinStockLevel())
				.maxStockLevel(request.getMaxStockLevel())
				.reorderPoint(request.getReorderPoint())
				.storageCondition(request.getStorageCondition())
				.imageUrl(request.getImageUrl()).category(category).supplier(supplier)
				.build();

		Product savedProduct = productRepository.save(product);

		return ProductDTO.fromEntity(savedProduct);
	}

	@Override
	public ProductDTO getProductById(Long id) {
		return ProductDTO.fromEntity(productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found")));
	}

	@Override
	public PageResponse<ProductDTO> getAllProducts(String query, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);

		Page<Product> products = query != null && !query.trim().isEmpty()
				? productRepository.findByNameContaining(query, pageable)
				: productRepository.findAll(pageable);

		return PageResponse.<ProductDTO> builder()
				.docs(products.stream().map(ProductDTO::fromEntity).toList())
				.totalDocs(products.getTotalElements()).page(page).limit(size)
				.totalPages(products.getTotalPages()).hasNextPage(products.hasNext())
				.hasPrevPage(products.hasPrevious()).build();
	}
}