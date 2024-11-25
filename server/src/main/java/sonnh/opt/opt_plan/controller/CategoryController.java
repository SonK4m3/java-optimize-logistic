package sonnh.opt.opt_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Category;
import sonnh.opt.opt_plan.service.CategoryService;
import sonnh.opt.opt_plan.payload.request.CategoryRequest;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.CategoryDTO;
import sonnh.opt.opt_plan.payload.response.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<CategoryDTO>>> getAllCategories(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		PageRequest pageRequest = PageRequest.of(page - 1, size);
		Page<Category> categories = categoryService.findAll(pageRequest);

		PageResponse<CategoryDTO> pageResponse = PageResponse
				.of(categories.map(CategoryDTO::fromEntity));

		return ResponseEntity.ok(ApiResponse.success(pageResponse));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(CategoryDTO.fromEntity(categoryService.findById(id)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
			@Valid @RequestBody CategoryRequest request) {
		Category category = new Category();
		category.setName(request.getName());
		return ResponseEntity.ok(ApiResponse
				.success(CategoryDTO.fromEntity(categoryService.create(category))));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id,
			@Valid @RequestBody CategoryRequest request) {

		Category category = new Category();
		category.setName(request.getName());
		return ResponseEntity.ok(ApiResponse
				.success(CategoryDTO.fromEntity(categoryService.update(id, category))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
	}
}