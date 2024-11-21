package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.CustomerAddress;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

	/**
	 * Find all addresses for a specific customer
	 * 
	 * @param customerId the customer's ID
	 * @return List of customer addresses
	 */
	List<CustomerAddress> findByCustomerId(Long customerId);

	/**
	 * Find the default address for a customer
	 * 
	 * @param customerId the customer's ID
	 * @param isDefault  true for default address
	 * @return Optional of CustomerAddress
	 */
	Optional<CustomerAddress> findByCustomerIdAndIsDefault(Long customerId,
			Boolean isDefault);

	/**
	 * Find addresses by customer ID and address type
	 * 
	 * @param customerId  the customer's ID
	 * @param addressType type of address (e.g., HOME, WORK)
	 * @return List of customer addresses
	 */
	List<CustomerAddress> findByCustomerIdAndAddressType(Long customerId,
			String addressType);
}