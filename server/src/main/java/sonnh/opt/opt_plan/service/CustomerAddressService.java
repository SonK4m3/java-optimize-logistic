package sonnh.opt.opt_plan.service;

import sonnh.opt.opt_plan.payload.request.CustomerAddressRequest;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.CustomerAddress;

import java.util.List;

public interface CustomerAddressService {
	CustomerAddress setNewDefaultAddress(Long customerId, Long addressId)
			throws ResourceNotFoundException;

	CustomerAddress addNewAddress(Long customerId, CustomerAddressRequest request);

	List<CustomerAddress> getCustomerAddresses(Long customerId);

	void deleteAddress(Long customerId, Long addressId);
}