package sonnh.opt.opt_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sonnh.opt.opt_plan.model.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUser_Id(Long userId);

	Optional<Customer> findByPhone(String phone);

	@Query("SELECT c FROM Customer c LEFT JOIN FETCH c.addresses WHERE c.id = :id")
	Optional<Customer> findByIdWithAddresses(Long id);

	@Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.addresses")
	List<Customer> findAllWithAddresses();
}