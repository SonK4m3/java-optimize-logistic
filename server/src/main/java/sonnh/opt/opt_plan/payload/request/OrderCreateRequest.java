package sonnh.opt.opt_plan.payload.request;

import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.ServiceType;
import sonnh.opt.opt_plan.constant.enums.PayerType;
import sonnh.opt.opt_plan.constant.enums.PickupTimeType;
import sonnh.opt.opt_plan.constant.enums.CargoType;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class OrderCreateRequest {
	// Sender information
	@NotNull
	private Long senderId;
	// Receiver information
	@NotBlank
	@Size(min = 2, message = "Name must be at least 2 characters")
	private String receiverName;

	@NotBlank
	@Size(min = 10, message = "Please enter a valid phone number")
	private String receiverPhone;

	@NotBlank
	@Size(min = 5, message = "Please enter a valid address")
	private String receiverAddress;

	@NotNull
	private double receiverLatitude;

	@NotNull
	private double receiverLongitude;

	@NotNull
	private Long pickupWarehouseId;

	@NotNull
	private PickupTimeType pickupTime;

	// Products
	@NotEmpty(message = "At least one product is required")
	private List<OrderProductRequest> orderProducts;

	@NotNull
	private ServiceType serviceType;

	@NotNull
	private CargoType cargoType;

	@NotNull
	private PayerType payer;

	private String deliveryNote;

	@NotNull
	private double weight;

	@NotNull
	private double totalPrice;
}