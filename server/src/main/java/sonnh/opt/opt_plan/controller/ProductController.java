package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.ProductDTO;
import sonnh.opt.opt_plan.payload.request.ProductCreateRequest;
import sonnh.opt.opt_plan.payload.response.PageResponse;
import sonnh.opt.opt_plan.service.ProductService;
import sonnh.opt.opt_plan.constant.common.Api;

@RestController
@RequestMapping(Api.PRODUCT_ROUTE)
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
	private final ProductService productService;

	@Operation(summary = "Create a new product")
	@PostMapping
	public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
			@Valid @RequestBody ProductCreateRequest request) {
		ProductDTO product = productService.createProduct(request);
		return ResponseEntity
				.ok(ApiResponse.success("Product created successfully", product));
	}

	@Operation(summary = "Get product by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
		ProductDTO product = productService.getProductById(id);
		return ResponseEntity.ok(ApiResponse.success(product));
	}

	@Operation(summary = "Get all products")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getAllProducts(
			@RequestParam(required = false) String query,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		PageResponse<ProductDTO> products = productService.getAllProducts(query, page,
				size);
		return ResponseEntity.ok(ApiResponse.success(products));
	}
}