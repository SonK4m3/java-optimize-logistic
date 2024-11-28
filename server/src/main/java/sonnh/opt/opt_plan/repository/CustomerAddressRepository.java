package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.CustomerAddress;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

	/**
	 * Find all addresses for a specific customer with optimized query
	 * 
	 * @param customerId the customer's ID
	 * @return List of customer addresses
	 */
	@Query("SELECT ca FROM CustomerAddress ca WHERE ca.customer.id = :customerId")
	List<CustomerAddress> findByCustomerId(@Param("customerId") Long customerId);

	/**
	 * Find the default address for a customer with optimized query
	 * 
	 * @param customerId the customer's ID
	 * @param isDefault  true for default address
	 * @return Optional of CustomerAddress
	 */
	@Query("SELECT ca FROM CustomerAddress ca WHERE ca.customer.id = :customerId AND ca.isDefault = :isDefault")
	Optional<CustomerAddress> findByCustomerIdAndIsDefault(
			@Param("customerId") Long customerId, @Param("isDefault") Boolean isDefault);

	/**
	 * Find address by ID and customer ID with optimized query
	 * 
	 * @param addressId  the address's ID
	 * @param customerId the customer's ID
	 * @return Optional of CustomerAddress
	 */
	@Query("SELECT ca FROM CustomerAddress ca WHERE ca.id = :addressId AND ca.customer.id = :customerId")
	Optional<CustomerAddress> findByIdAndCustomerId(@Param("addressId") Long addressId,
			@Param("customerId") Long customerId);

	/**
	 * Find first address by customer ID excluding specific address ID
	 */
	@Query("SELECT ca FROM CustomerAddress ca WHERE ca.customer.id = :customerId AND ca.id != :addressId ORDER BY ca.id ASC LIMIT 1")
	Optional<CustomerAddress> findFirstByCustomerIdAndIdNot(
			@Param("customerId") Long customerId, @Param("addressId") Long addressId);

	/**
	 * Count addresses for a customer with optimized query
	 */
	@Query("SELECT COUNT(ca) FROM CustomerAddress ca WHERE ca.customer.id = :customerId")
	Integer countByCustomerId(@Param("customerId") Long customerId);

	@Modifying
	@Query("UPDATE CustomerAddress ca SET ca.isDefault = false WHERE ca.customer.id = :customerId")
	void resetDefaultAddresses(@Param("customerId") Long customerId);
}