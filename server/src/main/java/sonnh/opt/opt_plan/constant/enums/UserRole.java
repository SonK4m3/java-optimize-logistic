
package sonnh.opt.opt_plan.constant.enums;

public enum UserRole {
    ADMIN("ROLE_ADMIN"), MANAGER("ROLE_MANAGER"), CUSTOMER("ROLE_CUSTOMER"),
    DRIVER("ROLE_DRIVER"), STAFF("ROLE_STAFF");

    private final String role;

    UserRole(String role) { this.role = role; }

    public String getRole() { return role; }
}