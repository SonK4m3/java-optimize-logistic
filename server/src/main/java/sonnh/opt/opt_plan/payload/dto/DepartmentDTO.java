package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.model.Department;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
	private Long id;
	private String code;
	private String name;
	private String description;
	private StaffDTO manager;

	public static DepartmentDTO fromEntity(Department department) {
		if (department == null)
			return null;
		return DepartmentDTO.builder().id(department.getId()).code(department.getCode())
				.name(department.getName()).description(department.getDescription())
				.manager(StaffDTO.fromEntity(department.getManager())).build();
	}
}
