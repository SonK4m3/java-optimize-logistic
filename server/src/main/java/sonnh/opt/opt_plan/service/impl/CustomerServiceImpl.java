package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.CustomerAddress;
import sonnh.opt.opt_plan.payload.request.CustomerUpdateRequest;
import sonnh.opt.opt_plan.repository.CustomerRepository;
import sonnh.opt.opt_plan.repository.CustomerAddressRepository;
import sonnh.opt.opt_plan.service.CustomerService;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
	private final CustomerRepository customerRepository;
	private final CustomerAddressRepository customerAddressRepository;
	private final UserRepository userRepository;

	@Override
	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public Customer updateCustomer(Long userId, Customer customer) {
		return customerRepository.findByUser_Id(userId).map(existingCustomer -> {
			existingCustomer.setPhone(customer.getPhone());
			return customerRepository.save(existingCustomer);
		}).orElseThrow(
				() -> new RuntimeException("Customer not found with userId: " + userId));
	}

	@Override
	public void deleteCustomer(Long userId) {
		customerRepository.findByUser_Id(userId).ifPresent(customerRepository::delete);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> getCustomerByUserId(Long userId) {
		return customerRepository.findByUser_Id(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> getCustomerByPhone(String phone) {
		return customerRepository.findByPhone(phone);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> getCustomerWithAddresses(Long customerId) {
		return customerRepository.findByIdWithAddresses(customerId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> getAllCustomersWithAddresses() {
		return customerRepository.findAllWithAddresses();
	}

	@Override
	public CustomerAddress addAddress(Long customerId, CustomerAddress address) {
		return customerRepository.findById(customerId).map(customer -> {
			address.setCustomer(customer);
			customer.getAddresses().add(address);
			customerRepository.save(customer);
			return address;
		}).orElseThrow(
				() -> new RuntimeException("Customer not found with id: " + customerId));
	}

	@Override
	public void removeAddress(Long customerId, Long addressId) {
		customerRepository.findById(customerId).ifPresent(customer -> customer
				.getAddresses().removeIf(address -> address.getId().equals(addressId)));
	}

	@Override
	public Customer updateCustomerInfo(Long userId,
			CustomerUpdateRequest customerUpdateRequest) {

		User user = userRepository.findById(userId).orElseThrow(
				() -> new RuntimeException("User not found with id: " + userId));

		CustomerAddress address = CustomerAddress.builder()
				.address(customerUpdateRequest.getAddress().getAddress())
				.city(customerUpdateRequest.getAddress().getCity())
				.country(customerUpdateRequest.getAddress().getCountry())
				.addressType(customerUpdateRequest.getAddress().getAddressType())
				.recipientInfo(customerUpdateRequest.getAddress().getRecipientInfo())
				.isDefault(customerUpdateRequest.getAddress().getIsDefault()).build();

		address = customerAddressRepository.save(address);

		List<CustomerAddress> addresses = new ArrayList<>();
		addresses.add(address);

		Customer customer = Customer.builder().phone(customerUpdateRequest.getPhone())
				.addresses(addresses).user(user).build();

		return customerRepository.save(customer);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> getAllCustomers() { return customerRepository.findAll(); }
}