package sonnh.opt.opt_plan.model.builder;

import sonnh.opt.opt_plan.model.Staff;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.Department;
import sonnh.opt.opt_plan.constant.enums.Position;
import sonnh.opt.opt_plan.constant.enums.UserRole;

/**
 * Builder class for creating Staff instances with predefined configurations
 */
public class StaffBuilder {

	/**
	 * Creates a base builder for Staff with default values
	 * 
	 * @return Staff instance with default values: - User with isActive: true
	 *         and role: MANAGER
	 */
	public static Staff createDefaultStaff() {
		User user = User.builder().isActive(true).role(UserRole.MANAGER).build();

		return Staff.builder().user(user).build();
	}

	/**
	 * Creates a basic staff member with required fields
	 * 
	 * @param username Staff's username for authentication
	 * @param email    Staff's email address
	 * @param password Staff's encrypted password
	 * @param fullName Staff's full name
	 * @param code     Staff's unique code
	 * @param position Staff's position in organization
	 * @return Staff instance configured with basic information
	 */
	public static Staff createBasicStaff(String username, String email, String password,
			String fullName, String code, Position position) {
		User user = User.builder().username(username).email(email).password(password)
				.fullName(fullName).isActive(true).role(UserRole.MANAGER).build();

		return Staff.builder().user(user).position(position).build();
	}

	/**
	 * Creates a staff member with department assignment
	 * 
	 * @param username   Staff's username
	 * @param email      Staff's email
	 * @param password   Staff's password
	 * @param fullName   Staff's full name
	 * @param code       Staff's code
	 * @param position   Staff's position
	 * @param department Staff's assigned department
	 * @return Staff instance with department assignment
	 */
	public static Staff createDepartmentStaff(String username, String email,
			String password, String fullName, String code, Position position,
			Department department) {
		User user = User.builder().username(username).email(email).password(password)
				.fullName(fullName).isActive(true).role(UserRole.MANAGER).build();

		return Staff.builder().user(user).position(position).department(department)
				.build();
	}
}