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
	private VehicleType vehicleType;
	private String vehiclePlateNumber;
	private LocalTime workStartTime;
	private LocalTime workEndTime;
	private Integer remainingWorkingMinutes;
	private String preferredAreas;
	private Integer completedDeliveries;
	private Double averageRating;
	private Double currentLatitude;
	private Double currentLongitude;
	private Boolean isActive;

	public static DriverDTO fromEntity(Driver driver) {
		if (driver == null)
			return null;
		return DriverDTO.builder().id(driver.getId()).driverCode(driver.getDriverCode())
				.fullName(driver.getFullName()).phone(driver.getPhone())
				.status(driver.getStatus()).vehicleType(driver.getVehicleType())
				.vehiclePlateNumber(driver.getVehiclePlateNumber())
				.workStartTime(driver.getWorkStartTime())
				.workEndTime(driver.getWorkEndTime())
				.remainingWorkingMinutes(driver.getRemainingWorkingMinutes())
				.preferredAreas(driver.getPreferredAreas())
				.completedDeliveries(driver.getCompletedDeliveries())
				.averageRating(driver.getAverageRating())
				.currentLatitude(driver.getCurrentLatitude())
				.currentLongitude(driver.getCurrentLongitude())
				.isActive(driver.getIsActive()).build();
	}
}