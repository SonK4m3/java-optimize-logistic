package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.model.Driver;
import java.time.LocalTime;

@Data
@Builder
public class DriverDTO {
	private Long id;
	private String driverCode;
	private String fullName;
	private String phone;
	private DriverStatus status;
	private VehicleType type;
	private String vehiclePlateNumber;
	private LocalTime workStartTime;
	private LocalTime workEndTime;
	private Double currentLatitude;
	private Double currentLongitude;

	public static DriverDTO fromEntity(Driver driver) {
		if (driver == null)
			return null;
		return DriverDTO.builder().id(driver.getId()).driverCode(driver.getDriverCode())
				.fullName(driver.getFullName()).phone(driver.getPhone())
				.status(driver.getStatus()).type(driver.getType())
				.vehiclePlateNumber(driver.getVehiclePlateNumber())
				.workStartTime(driver.getWorkStartTime())
				.workEndTime(driver.getWorkEndTime())
				.currentLatitude(driver.getCurrentLatitude())
				.currentLongitude(driver.getCurrentLongitude()).build();
	}
}