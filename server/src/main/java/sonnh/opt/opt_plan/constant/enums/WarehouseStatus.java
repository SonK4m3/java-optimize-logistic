package sonnh.opt.opt_plan.constant.enums;

public enum WarehouseStatus {
	ACTIVE("Đang hoạt động"), INACTIVE("Ngừng hoạt động"), MAINTENANCE("Đang bảo trì"),
	FULL("Đã đầy"), CLOSED("Đã đóng cửa");

	private final String description;

	WarehouseStatus(String description) { this.description = description; }

	public String getDescription() { return description; }
}