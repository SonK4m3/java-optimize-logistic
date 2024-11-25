package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.Position;
import sonnh.opt.opt_plan.model.Staff;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
	private Long id;
	private UserDTO user;
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

		return StaffDTO.builder().id(staff.getId())
				.user(UserDTO.fromEntity(staff.getUser())).position(staff.getPosition())
				.department(DepartmentDTO.fromEntity(staff.getDepartment())).build();
	}
}
