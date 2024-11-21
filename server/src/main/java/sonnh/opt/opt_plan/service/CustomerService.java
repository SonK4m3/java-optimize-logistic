package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.CustomerAddress;
import sonnh.opt.opt_plan.payload.request.CustomerUpdateRequest;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
	Customer createCustomer(Customer customer);

	Customer updateCustomer(Long userId, Customer customer);

	void deleteCustomer(Long userId);

	Optional<Customer> getCustomerByUserId(Long userId);

	Optional<Customer> getCustomerByPhone(String phone);

	Optional<Customer> getCustomerWithAddresses(Long customerId);

	List<Customer> getAllCustomersWithAddresses();

	CustomerAddress addAddress(Long customerId, CustomerAddress address);

	void removeAddress(Long customerId, Long addressId);

	Customer updateCustomerInfo(Long userId, CustomerUpdateRequest customerUpdateRequest);

	List<Customer> getAllCustomers();
}