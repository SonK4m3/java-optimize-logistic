package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.SupplierStatus;
import sonnh.opt.opt_plan.model.Supplier;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
	private Long id;
	private String name;
	private SupplierStatus status;
	private Integer productCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static SupplierDTO fromEntity(Supplier supplier) {
		if (supplier == null)
			return null;
		return SupplierDTO.builder().id(supplier.getId()).name(supplier.getName())
				.productCount(
						supplier.getProducts() != null ? supplier.getProducts().size()
								: 0)
				.createdAt(supplier.getCreatedAt()).updatedAt(supplier.getUpdatedAt())
				.status(supplier.getStatus()).build();
	}
}