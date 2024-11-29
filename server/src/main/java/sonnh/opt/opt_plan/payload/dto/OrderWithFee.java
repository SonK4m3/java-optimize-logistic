package sonnh.opt.opt_plan.payload.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import sonnh.opt.opt_plan.constant.enums.DeliveryServiceType;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.DeliveryFee;
import sonnh.opt.opt_plan.model.Order;

@Data
@Builder
public class OrderWithFee {
	private Long orderId;
	private String orderCode;
	private OrderStatus status;
	private OrderPriority priority;
	private Double subtotal;
	private DeliveryFeeDetails deliveryFee;
	private Double totalAmount;
	private Double totalWeight;

	private Long customerId;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdated;

	private DeliveryDTO delivery;

	@Data
	@Builder
	public static class DeliveryFeeDetails {
		private Double baseFee;
		private Double weightFee;
		private Double surcharge;
		private Double totalFee;
		private DeliveryServiceType serviceType;
	}

	public static OrderWithFee fromEntity(Order order) {
		Delivery delivery = order.getDelivery();
		DeliveryFee deliveryFee = delivery != null ? delivery.getDeliveryFee() : null;

		return OrderWithFee.builder().orderId(order.getId())
				.orderCode(order.getOrderCode()).status(order.getStatus())
				.priority(order.getPriority())
				.subtotal(order.getTotalAmount()
						- (deliveryFee != null ? deliveryFee.getTotalFee() : 0))
				.deliveryFee(DeliveryFeeDetails.builder()
						.baseFee(deliveryFee != null ? deliveryFee.getBaseFee() : 0)
						.weightFee(deliveryFee != null ? deliveryFee.getWeightFee() : 0)
						.surcharge(deliveryFee != null ? deliveryFee.getSurcharge() : 0)
						.totalFee(deliveryFee != null ? deliveryFee.getTotalFee() : 0)
						.serviceType(delivery != null ? delivery.getServiceType() : null)
						.build())
				.totalAmount(order.getTotalAmount()).totalWeight(order.getTotalWeight())
				.customerId(order.getCustomer().getId()).createdAt(order.getCreatedAt())
				.lastUpdated(order.getLastUpdated())
				.delivery(DeliveryDTO.fromEntity(delivery)).build();
	}
}
