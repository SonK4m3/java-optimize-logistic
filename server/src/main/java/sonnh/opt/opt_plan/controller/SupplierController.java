package sonnh.opt.opt_plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sonnh.opt.opt_plan.model.Supplier;
import sonnh.opt.opt_plan.service.SupplierService;
import sonnh.opt.opt_plan.constant.enums.SupplierStatus;
import sonnh.opt.opt_plan.payload.request.SupplierRequest;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.SupplierDTO;
import sonnh.opt.opt_plan.payload.response.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

	private final SupplierService supplierService;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<SupplierDTO>>> getAllSuppliers(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		PageRequest pageRequest = PageRequest.of(page - 1, size);
		Page<Supplier> suppliers = supplierService.findAll(pageRequest);

		PageResponse<SupplierDTO> pageResponse = PageResponse
				.of(suppliers.map(SupplierDTO::fromEntity));

		return ResponseEntity.ok(ApiResponse.success(pageResponse));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<SupplierDTO>> getSupplierById(
			@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse
				.success(SupplierDTO.fromEntity(supplierService.findById(id))));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<SupplierDTO>> createSupplier(
			@Valid @RequestBody SupplierRequest request) {
		Supplier supplier = Supplier.builder().name(request.getName())
				.status(SupplierStatus.ACTIVE).build();

		return ResponseEntity.ok(ApiResponse
				.success(SupplierDTO.fromEntity(supplierService.create(supplier))));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<SupplierDTO>> updateSupplier(@PathVariable Long id,
			@Valid @RequestBody SupplierRequest request) {

		Supplier supplier = Supplier.builder().name(request.getName())
				.status(request.getStatus()).build();

		return ResponseEntity.ok(ApiResponse
				.success(SupplierDTO.fromEntity(supplierService.update(id, supplier))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteSupplier(@PathVariable Long id) {
		supplierService.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully"));
	}
}