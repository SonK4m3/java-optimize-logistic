package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sonnh.opt.opt_plan.model.ReceiptDetail;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemDTO {
	private Long id;
	private Long productId;
	private String productCode;
	private String productName;
	private Integer quantity;
	private String note;

	public static ReceiptItemDTO fromEntity(ReceiptDetail receiptDetail) {
		return ReceiptItemDTO.builder().id(receiptDetail.getId())
				.productId(receiptDetail.getProduct().getId())
				.productCode(receiptDetail.getProduct().getCode())
				.productName(receiptDetail.getProduct().getName())
				.quantity(receiptDetail.getQuantity()).note(receiptDetail.getNote())
				.build();
	}
}
