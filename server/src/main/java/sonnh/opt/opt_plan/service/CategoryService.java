package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.model.Category;
import sonnh.opt.opt_plan.repository.CategoryRepository;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	public Category findById(Long id) {
		return categoryRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	@Transactional
	public Category create(Category category) {
		if (categoryRepository.existsByName(category.getName())) {
			throw new IllegalArgumentException("Category name already exists");
		}
		return categoryRepository.save(category);
	}

	@Transactional
	public Category update(Long id, Category category) {
		Category existing = findById(id);

		if (!existing.getName().equals(category.getName())
				&& categoryRepository.existsByName(category.getName())) {
			throw new IllegalArgumentException("Category name already exists");
		}

		category.setId(id);
		return categoryRepository.save(category);
	}

	@Transactional
	public void delete(Long id) {
		if (!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Category not found with id: " + id);
		}
		categoryRepository.deleteById(id);
	}
}