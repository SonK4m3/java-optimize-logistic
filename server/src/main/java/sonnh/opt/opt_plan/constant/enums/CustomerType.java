package sonnh.opt.opt_plan.constant.enums;

public enum CustomerType {
	INDIVIDUAL("Individual Customer"), CORPORATE("Corporate Customer");

	private final String description;

	CustomerType(String description) { this.description = description; }

	public String getDescription() { return description; }
}