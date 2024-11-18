package sonnh.opt.opt_plan.payload.dto;

import lombok.Builder;
import lombok.Data;

import sonnh.opt.opt_plan.model.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class OrderSuggestion {
	private Order order;
	private Double distance;
	private Double estimatedCost;
	private Double estimatedEarnings;
	private Double estimatedProfit;
	private Double score;
	private String routeSummary;
	private List<String> optimizationTips;

	@Builder.Default
	private Map<String, Double> costBreakdown = new HashMap<>();

	public void addCostItem(String item, Double amount) {
		costBreakdown.put(item, amount);
	}
}