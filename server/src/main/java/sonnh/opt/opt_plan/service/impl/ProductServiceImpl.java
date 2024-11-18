package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Product;
import sonnh.opt.opt_plan.payload.dto.ProductDTO;
import sonnh.opt.opt_plan.payload.request.ProductCreateRequest;
import sonnh.opt.opt_plan.repository.ProductRepository;
import sonnh.opt.opt_plan.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;

	@Override
	@Transactional
	public ProductDTO createProduct(ProductCreateRequest request) {
		Product product = Product.builder().build();
		return ProductDTO.fromEntity(productRepository.save(product));
	}

	@Override
	public ProductDTO getProductById(Long id) {
		return ProductDTO.fromEntity(productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found")));
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll().stream().map(ProductDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> getActiveProducts() {
		return productRepository.findByIsActiveTrue().stream().map(ProductDTO::fromEntity)
				.collect(Collectors.toList());
	}

}