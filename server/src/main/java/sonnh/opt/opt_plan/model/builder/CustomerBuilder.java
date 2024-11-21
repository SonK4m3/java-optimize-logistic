package sonnh.opt.opt_plan.model.builder;

import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.constant.enums.UserRole;

/**
 * Builder class for creating Customer instances with predefined configurations
 */
public class CustomerBuilder {

	/**
	 * Creates a base builder for Customer with default values
	 * 
	 * @return Customer instance with default values: - isActive: true - role:
	 *         CUSTOMER - phone: null
	 */
	public static Customer createDefaultCustomer() {
		Customer customer = new Customer();
		customer.setUser(User.builder().isActive(true).role(UserRole.CUSTOMER).build());
		return customer;
	}

	/**
	 * Creates an individual customer with required fields
	 * 
	 * @param username       Customer's username for authentication
	 * @param email          Customer's email address
	 * @param password       Customer's encrypted password
	 * @param fullName       Customer's full name
	 * @param phone          Customer's phone number
	 * @param identityNumber Customer's identity/passport number
	 * @return Customer instance configured for individual use
	 */
	public static Customer createIndividualCustomer(String username, String email,
			String password, String fullName, String phone, String identityNumber) {
		Customer customer = createDefaultCustomer();
		customer.getUser().setUsername(username);
		customer.getUser().setEmail(email);
		customer.getUser().setPassword(password);
		customer.getUser().setFullName(fullName);
		customer.setPhone(phone);
		return customer;
	}

	/**
	 * Creates a corporate customer with required fields
	 * 
	 * @param username    Corporate customer's username
	 * @param email       Corporate email address
	 * @param password    Account password
	 * @param fullName    Company representative's name
	 * @param phone       Contact phone number
	 * @param companyName Registered company name
	 * @param taxCode     Company tax identification number
	 * @return Customer instance configured for corporate use
	 */
	public static Customer createCorporateCustomer(String username, String email,
			String password, String fullName, String phone, String companyName,
			String taxCode) {
		Customer customer = createDefaultCustomer();
		customer.getUser().setUsername(username);
		customer.getUser().setEmail(email);
		customer.getUser().setPassword(password);
		customer.getUser().setFullName(fullName);
		customer.setPhone(phone);
		return customer;
	}
}