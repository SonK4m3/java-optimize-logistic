package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.model.Supplier;
import sonnh.opt.opt_plan.repository.SupplierRepository;
import sonnh.opt.opt_plan.constant.enums.SupplierStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class SupplierService {

	private final SupplierRepository supplierRepository;

	public Page<Supplier> findAll(Pageable pageable) {
		return supplierRepository.findAll(pageable);
	}

	public Supplier findById(Long id) {
		return supplierRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Supplier not found with id: " + id));
	}

	@Transactional
	public Supplier create(Supplier supplier) {
		if (supplierRepository.existsByName(supplier.getName())) {
			throw new IllegalArgumentException("Supplier name already exists");
		}
		return supplierRepository.save(supplier);
	}

	@Transactional
	public Supplier update(Long id, Supplier supplier) {
		Supplier existing = findById(id);

		if (!existing.getName().equals(supplier.getName())
				&& supplierRepository.existsByName(supplier.getName())) {
			throw new IllegalArgumentException("Supplier name already exists");
		}

		supplier.setId(id);
		return supplierRepository.save(supplier);
	}

	@Transactional
	public void delete(Long id) {
		Supplier supplier = findById(id);
		supplier.setStatus(SupplierStatus.INACTIVE);
		supplierRepository.save(supplier);
	}
}