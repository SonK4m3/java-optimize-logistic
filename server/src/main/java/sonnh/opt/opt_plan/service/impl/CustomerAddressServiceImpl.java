package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Customer;
import sonnh.opt.opt_plan.model.CustomerAddress;
import sonnh.opt.opt_plan.repository.CustomerAddressRepository;
import sonnh.opt.opt_plan.repository.CustomerRepository;
import sonnh.opt.opt_plan.service.CustomerAddressService;
import sonnh.opt.opt_plan.payload.request.CustomerAddressRequest;
import sonnh.opt.opt_plan.model.Location;
import sonnh.opt.opt_plan.repository.LocationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {
	private final CustomerRepository customerRepository;
	private final CustomerAddressRepository addressRepository;
	private final LocationRepository locationRepository;

	@Override
	@Transactional
	public CustomerAddress setNewDefaultAddress(Long customerId, Long addressId) {
		// Find customer and validate
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		// Find address and validate ownership
		CustomerAddress newDefaultAddress = addressRepository
				.findByIdAndCustomerId(addressId, customer.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		// Reset all addresses to non-default
		addressRepository.resetDefaultAddresses(customer.getId());

		// Set new default address
		newDefaultAddress.setIsDefault(true);
		CustomerAddress savedAddress = addressRepository.save(newDefaultAddress);

		return savedAddress;
	}

	@Override
	@Transactional
	public CustomerAddress addNewAddress(Long customerId,
			CustomerAddressRequest request) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		// If this is the first address, make it default
		boolean isFirstAddress = addressRepository.countByCustomerId(customerId) == 0;

		Location location = Location.builder().address(request.getAddress())
				.latitude(request.getLatitude()).longitude(request.getLongitude())
				.build();

		location = locationRepository.save(location);

		CustomerAddress address = CustomerAddress.builder().customer(customer)
				.location(location).isDefault(isFirstAddress).build();

		CustomerAddress savedAddress = addressRepository.save(address);

		customer.getAddresses().add(savedAddress);
		customerRepository.save(customer);
		return savedAddress;
	}

	@Override
	public List<CustomerAddress> getCustomerAddresses(Long customerId) {
		List<CustomerAddress> addresses = addressRepository.findByCustomerId(customerId);
		return addresses;
	}

	@Override
	@Transactional
	public void deleteAddress(Long customerId, Long addressId) {
		CustomerAddress address = addressRepository
				.findByIdAndCustomerId(addressId, customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		if (address.getIsDefault()) {
			addressRepository.findFirstByCustomerIdAndIdNot(customerId, addressId)
					.ifPresent(newDefault -> {
						newDefault.setIsDefault(true);
						addressRepository.save(newDefault);
					});
		}

		addressRepository.delete(address);
	}
}