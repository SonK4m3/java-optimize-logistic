package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.model.Driver;
import java.time.LocalDateTime;

@Data
@Builder
public class DriverDTO {
	private Long id;
	private String fullName;
	private String phone;
	private String licenseNumber;
	private VehicleType vehicleType;
	private String vehiclePlate;
	private DriverStatus status;
	private Double currentLatitude;
	private Double currentLongitude;
	private LocalDateTime lastUpdated;

	public static DriverDTO fromEntity(Driver driver) {
		if (driver == null)
			return null;
		return DriverDTO.builder().id(driver.getId())
				.fullName(driver.getUser().getFullName()).phone(driver.getPhone())
				.licenseNumber(driver.getLicenseNumber())
				.vehicleType(driver.getVehicleType())
				.vehiclePlate(driver.getVehiclePlate()).status(driver.getStatus())
				.currentLatitude(driver.getCurrentLatitude())
				.currentLongitude(driver.getCurrentLongitude())
				.lastUpdated(driver.getLastUpdated()).build();
	}
}