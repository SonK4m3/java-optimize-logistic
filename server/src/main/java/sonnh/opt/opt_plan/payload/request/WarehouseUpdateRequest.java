
package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseUpdateRequest {
	/**
	 * Warehouse code (must be unique)
	 */
	@Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Code must be 3-10 characters long and contain only uppercase letters and numbers")
	private String code;

	/**
	 * Warehouse name
	 */
	@Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
	private String name;

	/**
	 * Physical address of the warehouse
	 */
	@Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
	private String address;

	/**
	 * Contact phone number
	 */
	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
	private String phone;

	/**
	 * Contact email address
	 */
	@Email(message = "Invalid email format")
	private String email;

	/**
	 * Geographical latitude
	 */
	@DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
	@DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
	private Double latitude;

	/**
	 * Geographical longitude
	 */
	@DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
	@DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
	private Double longitude;

	/**
	 * Warehouse active status
	 */
	private Boolean isActive;

	/**
	 * Total storage capacity in cubic meters
	 */
	@Min(value = 0, message = "Capacity must be greater than or equal to 0")
	@Max(value = 1000000, message = "Capacity must be less than or equal to 1,000,000")
	private Integer capacity;

	/**
	 * Current occupancy in cubic meters
	 */
	@Min(value = 0, message = "Current occupancy must be greater than or equal to 0")
	private Integer currentOccupancy;

	/**
	 * ID of the warehouse manager
	 */
	@Positive(message = "Manager ID must be positive")
	private Long managerId;

	/**
	 * Operating hours in format "HH:MM-HH:MM"
	 */
	@Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]-([0-1][0-9]|2[0-3]):[0-5][0-9]$", message = "Operating hours must be in format HH:MM-HH:MM")
	private String operatingHours;

	/**
	 * Security level (LOW, MEDIUM, HIGH)
	 */
	@Pattern(regexp = "^(LOW|MEDIUM|HIGH)$", message = "Security level must be LOW, MEDIUM, or HIGH")
	private String securityLevel;

	/**
	 * Temperature range in Celsius (format: "min-max")
	 */
	@Pattern(regexp = "^-?\\d+--?\\d+$", message = "Temperature range must be in format min-max")
	private String temperatureRange;

	/**
	 * Humidity range in percentage (format: "min-max")
	 */
	@Pattern(regexp = "^\\d+-\\d+$", message = "Humidity range must be in format min-max")
	private String humidityRange;

	/**
	 * Additional facilities (comma-separated)
	 */
	@Size(max = 500, message = "Facilities description must not exceed 500 characters")
	private String facilities;

	/**
	 * Special handling capabilities (comma-separated)
	 */
	@Size(max = 500, message = "Special handling capabilities must not exceed 500 characters")
	private String specialHandlingCapabilities;

	/**
	 * Validates that currentOccupancy does not exceed capacity
	 */
	@AssertTrue(message = "Current occupancy cannot exceed capacity")
	private boolean isOccupancyValid() {
		if (capacity == null || currentOccupancy == null) {
			return true;
		}
		return currentOccupancy <= capacity;
	}

	/**
	 * Validates temperature range format and values
	 */
	@AssertTrue(message = "Invalid temperature range")
	private boolean isTemperatureRangeValid() {
		if (temperatureRange == null) {
			return true;
		}
		try {
			String[] range = temperatureRange.split("-");
			int min = Integer.parseInt(range[0]);
			int max = Integer.parseInt(range[1]);
			return min <= max && min >= -50 && max <= 50;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Validates humidity range format and values
	 */
	@AssertTrue(message = "Invalid humidity range")
	private boolean isHumidityRangeValid() {
		if (humidityRange == null) {
			return true;
		}
		try {
			String[] range = humidityRange.split("-");
			int min = Integer.parseInt(range[0]);
			int max = Integer.parseInt(range[1]);
			return min <= max && min >= 0 && max <= 100;
		} catch (Exception e) {
			return false;
		}
	}
}