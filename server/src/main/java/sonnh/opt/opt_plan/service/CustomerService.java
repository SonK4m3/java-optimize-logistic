package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.CustomerAddress;
import sonnh.opt.opt_plan.payload.request.CustomerUpdateRequest;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
	Customer createCustomer(Customer customer);

	Optional<Customer> getCustomerByUserId(Long userId);

	CustomerAddress addAddress(Long customerId, CustomerAddress address);

	void removeAddress(Long customerId, Long addressId);

	Customer updateCustomerInfo(Long userId, CustomerUpdateRequest customerUpdateRequest);

	List<Customer> getAllCustomers();

	Optional<Customer> findById(Long id);
}