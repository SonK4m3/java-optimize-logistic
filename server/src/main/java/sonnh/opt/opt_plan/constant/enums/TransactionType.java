package sonnh.opt.opt_plan.constant.enums;

public enum TransactionType {
	INBOUND_PURCHASE("Nhập mua hàng"), INBOUND_RETURN("Nhập hàng trả lại"),
	INBOUND_TRANSFER("Nhập chuyển kho"), OUTBOUND_SALE("Xuất bán hàng"),
	OUTBOUND_RETURN("Xuất trả hàng"), OUTBOUND_TRANSFER("Xuất chuyển kho"),
	ADJUSTMENT_DAMAGE("Điều chỉnh hỏng"), ADJUSTMENT_LOSS("Điều chỉnh mất mát"),
	ADJUSTMENT_INVENTORY("Điều chỉnh kiểm kê");

	private final String description;

	TransactionType(String description) { this.description = description; }

	public String getDescription() { return description; }
}