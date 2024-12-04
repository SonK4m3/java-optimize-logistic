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
import sonnh.opt.opt_plan.model.Location;
import sonnh.opt.opt_plan.repository.UserRepository;
import sonnh.opt.opt_plan.repository.LocationRepository;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
	private final CustomerRepository customerRepository;
	private final CustomerAddressRepository customerAddressRepository;
	private final UserRepository userRepository;
	private final LocationRepository locationRepository;

	/**
	 * Creates a new customer with validation
	 * 
	 * @param customer Customer object to be created
	 * @return Created customer
	 * @throws IllegalArgumentException if customer data is invalid
	 */
	@Override
	public Customer createCustomer(Customer customer) {
		validateCustomerData(customer);
		return customerRepository.save(customer);
	}

	/**
	 * Gets customer by user ID with optimized query
	 * 
	 * @param userId ID of the user
	 * @return Optional containing customer if found
	 * @throws ResourceNotFoundException if user not found
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> getCustomerByUserId(Long userId) {
		return customerRepository.findByUserId(userId).or(() -> {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"User not found with id: " + userId));
			return customerRepository.findByUser(user);
		});
	}

	/**
	 * Adds a new address for a customer with validation
	 * 
	 * @param customerId ID of the customer
	 * @param address    Address to be added
	 * @return Added customer address
	 * @throws ResourceNotFoundException if customer not found
	 */
	@Override
	public CustomerAddress addAddress(Long customerId, CustomerAddress address) {
		Customer customer = customerRepository.findByIdWithAddresses(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found with id: " + customerId));

		validateAddress(address);

		if (address.getIsDefault()) {
			customer.getAddresses().forEach(addr -> addr.setIsDefault(false));
		}

		address.setCustomer(customer);
		customer.getAddresses().add(address);

		return customerAddressRepository.save(address);
	}

	/**
	 * Removes an address from a customer with validation
	 * 
	 * @param customerId ID of the customer
	 * @param addressId  ID of the address to remove
	 * @throws ResourceNotFoundException if customer or address not found
	 * @throws IllegalArgumentException  if address doesn't belong to customer
	 */
	@Override
	public void removeAddress(Long customerId, Long addressId) {
		CustomerAddress address = customerAddressRepository
				.findByIdAndCustomerId(addressId, customerId).orElseThrow(
						() -> new ResourceNotFoundException("Address not found with id: "
								+ addressId + " for customer: " + customerId));

		if (address.getIsDefault()) {
			customerAddressRepository.findFirstByCustomerIdAndIdNot(customerId, addressId)
					.ifPresent(newDefault -> {
						newDefault.setIsDefault(true);
						customerAddressRepository.save(newDefault);
					});
		}

		customerAddressRepository.delete(address);
	}

	/**
	 * Updates customer information with validation
	 * 
	 * @param userId                ID of the user
	 * @param customerUpdateRequest Request containing updated information
	 * @return Updated customer
	 * @throws ResourceNotFoundException if customer not found
	 */
	@Override
	public Customer updateCustomerInfo(Long userId,
			CustomerUpdateRequest customerUpdateRequest) {
		Customer customer = getCustomerByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found for user id: " + userId));

		updateCustomerFields(customer, customerUpdateRequest);

		return customerRepository.save(customer);
	}

	/**
	 * Gets all customers with addresses
	 * 
	 * @return List of all customers with their addresses
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Customer> getAllCustomers() {
		return customerRepository.findAllWithAddresses();
	}

	private void validateCustomerData(Customer customer) {
		if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
			throw new IllegalArgumentException("Customer phone number is required");
		}
		// Add more validations as needed
	}

	private void validateAddress(CustomerAddress address) {
		if (address.getLocation() == null) {
			throw new IllegalArgumentException("Location cannot be empty");
		}
		if (address.getIsDefault() == null) {
			address.setIsDefault(false);
		}
	}

	private void updateCustomerFields(Customer customer, CustomerUpdateRequest request) {
		if (request.getPhone() != null) {
			customer.setPhone(request.getPhone());
		}

		if (request.getAddress().getLatitude() != null
				&& request.getAddress().getLongitude() != null) {
			Location location = Location.builder()
					.latitude(request.getAddress().getLatitude())
					.longitude(request.getAddress().getLongitude()).build();

			location = locationRepository.save(location);

			CustomerAddress address = CustomerAddress.builder().location(location)
					.isDefault(request.getAddress().getIsDefault()).customer(customer)
					.build();

			validateAddress(address);

			if (address.getIsDefault()) {
				customer.getAddresses().forEach(addr -> addr.setIsDefault(false));
			}

			customer.getAddresses().add(address);
		}
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return customerRepository.findById(id);
	}
}