package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	private Long id;
	private String name;

	public static CategoryDTO fromEntity(Category category) {
		if (category == null)
			return null;
		return CategoryDTO.builder().id(category.getId()).name(category.getName())
				.build();
	}
}