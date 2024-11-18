
package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.WarehouseType;
import java.time.LocalTime;

@Data
public class WarehouseCreateRequest {
	@NotBlank(message = "Warehouse code is required")
	private String code;

	@NotBlank(message = "Warehouse name is required")
	private String name;

	@NotBlank(message = "Address is required")
	private String address;

	@NotNull(message = "Latitude is required")
	private Double latitude;

	@NotNull(message = "Longitude is required")
	private Double longitude;

	@NotNull(message = "Total capacity is required")
	@Positive(message = "Total capacity must be positive")
	private Integer totalCapacity;

	@NotNull(message = "Warehouse type is required")
	private WarehouseType type;

	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String phone;
	private String email;
	private String contactPerson;
	private LocalTime openingTime;
	private LocalTime closingTime;
	private Boolean isOpen24Hours;
	private String workingDays;
	private Boolean hasLoadingDock;
	private Boolean hasRefrigeration;
	private Boolean hasSecuritySystem;
	private Double temperatureMin;
	private Double temperatureMax;
	private Double area;
}