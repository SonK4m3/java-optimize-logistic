package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.Position;
import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
	private Long id;
	private String code;
	private String username;
	private String fullName;
	private String email;
	private Boolean isActive;
	private Position position;
	private DepartmentDTO department;

	/**
	 * Convert Staff entity to StaffDTO
	 * 
	 * @param staff Staff entity to convert
	 * @return StaffDTO object with mapped fields
	 */
	public static StaffDTO fromEntity(Staff staff) {
		if (staff == null) {
			return null;
		}

		User user = staff.getUser();
		if (user == null) {
			return null;
		}

		return StaffDTO.builder().id(user.getId()).username(user.getUsername())
				.fullName(user.getFullName()).email(user.getEmail())
				.isActive(user.isActive()).position(staff.getPosition())
				.department(DepartmentDTO.fromEntity(staff.getDepartment())).build();
	}
}
