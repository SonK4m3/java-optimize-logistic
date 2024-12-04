package sonnh.opt.opt_plan.payload.request;

import java.time.LocalDateTime;
import java.util.Set;

import sonnh.opt.opt_plan.constant.enums.DeliveryPriority;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeliveryCreateRequest {
	@NotNull(message = "Weight is required")
	@Positive(message = "Weight must be positive")
	private Double weight;

	@NotBlank(message = "Receiver name is required")
	@Size(max = 100)
	private String receiverName;

	@NotBlank(message = "Receiver phone is required")
	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
	private String receiverPhone;

	@NotBlank(message = "Delivery address is required")
	private String deliveryAddress;

	@NotNull(message = "Latitude is required")
	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	private Double latitude;

	@NotNull(message = "Longitude is required")
	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	private Double longitude;

	@NotNull(message = "Requested delivery time is required")
	@Future(message = "Delivery time must be in the future")
	private LocalDateTime requestedDeliveryTime;

	@Size(max = 500)
	private String notes;

	// Optional fields for additional delivery requirements
	private DeliveryPriority priority;
	private Set<String> specialRequirements;

	/**
	 * Validates time window constraints
	 */
	@AssertTrue(message = "Delivery time must be within business hours")
	private boolean isValidDeliveryTime() {
		if (requestedDeliveryTime == null) {
			return true;
		}

		int hour = requestedDeliveryTime.getHour();
		return hour >= 8 && hour <= 18; // 8 AM - 6 PM
	}
}