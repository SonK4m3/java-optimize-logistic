package sonnh.opt.opt_plan.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderProductRequest {
	@NotNull
	private String name;

	@NotNull
	private Double price;

	@NotNull
	private Integer quantity;

	@NotNull
	private Double weight;
}
