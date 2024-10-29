package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.User;
import java.util.List;

public interface UserService {
	User createUser(User user);

	User updateUser(Long id, User user);

	User getUserById(Long id);

	User getUserByUsername(String username);

	List<User> getAllUsers();

	void deleteUser(Long id);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}