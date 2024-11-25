package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.*;
import sonnh.opt.opt_plan.model.Location;
import sonnh.opt.opt_plan.model.Order;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	private Long id;
	private String orderCode;
	private OrderStatus status;
	private OrderPriority priority;
	private CargoType cargoType;
	private PayerType payer;
	private ServiceType serviceType;

	private UserDTO sender;
	private Double weight;
	private Double totalPrice;
	private String receiverName;
	private String receiverPhone;
	private LocationDTO receiverLocation;
	private WarehouseDTO pickupWarehouse;
	private PickupTimeType pickupTime;
	private List<OrderProductDTO> orderProducts;
	private LocalDateTime lastUpdated;
	private String lastUpdatedBy;

	// Mapper method
	public static OrderDTO fromEntity(Order order) {
		if (order == null)
			return null;

		return OrderDTO.builder().id(order.getId()).orderCode(order.getOrderCode())
				.status(order.getStatus()).priority(order.getPriority())
				.cargoType(order.getCargoType()).payer(order.getPayer())
				.serviceType(order.getServiceType())
				.sender(UserDTO.fromEntity(order.getSender())).weight(order.getWeight())
				.totalPrice(order.getTotalPrice()).receiverName(order.getReceiverName())
				.receiverPhone(order.getReceiverPhone())
				.receiverLocation(LocationDTO.fromEntity(order.getReceiverLocation()))
				.pickupWarehouse(WarehouseDTO.fromEntity(order.getPickupWarehouse()))
				.pickupTime(order.getPickupTime())
				.orderProducts(
						order.getOrderProducts() != null
								? order.getOrderProducts().stream()
										.map(OrderProductDTO::fromEntity).toList()
								: null)
				.lastUpdated(order.getLastUpdated())
				.lastUpdatedBy(order.getLastUpdatedBy()).build();
	}
}