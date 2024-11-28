package sonnh.opt.opt_plan.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.OrderPriority;
import sonnh.opt.opt_plan.constant.enums.OrderStatus;
import sonnh.opt.opt_plan.model.Order;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Order entity.
 * 
 * @author Son Nguyen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	private Long id;
	private String orderCode;
	private OrderStatus status;
	private OrderPriority priority;
	private Double totalAmount;
	private Double totalWeight;

	private CustomerDTO customer;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdated;

	/**
	 * Maps an Order entity to an OrderDTO.
	 * 
	 * @param order the Order entity to map.
	 * @return the mapped OrderDTO.
	 */
	public static OrderDTO fromEntity(Order order) {
		if (order == null)
			return null;

		return OrderDTO.builder().id(order.getId()).orderCode(order.getOrderCode())
				.status(order.getStatus()).priority(order.getPriority())
				.totalAmount(order.getTotalAmount()).totalWeight(order.getTotalWeight())
				.customer(CustomerDTO.fromEntity(order.getCustomer()))
				.createdAt(order.getCreatedAt()).lastUpdated(order.getLastUpdated())
				.build();
	}
}