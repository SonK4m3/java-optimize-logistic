package sonnh.opt.opt_plan.constant.enums;

public enum SupplierStatus {
	ACTIVE("Đang hoạt động"), INACTIVE("Ngừng hoạt động"), BLACKLISTED("Danh sách đen"),
	PENDING_REVIEW("Đang xem xét"), SUSPENDED("Tạm ngưng");

	private final String description;

	SupplierStatus(String description) { this.description = description; }

	public String getDescription() { return description; }
}