package sonnh.opt.opt_plan.service;

import java.util.List;

import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Route;
import sonnh.opt.opt_plan.model.Vehicle;

public interface RouteOptimizationService {
	/**
	 * Tối ưu routes cho các delivery với xe có sẵn Input: Danh sách delivery
	 * cần giao, danh sách xe khả dụng Output: Danh sách routes được tối ưu
	 */
	List<Route> optimizeRoutes(List<Delivery> deliveries, List<Vehicle> vehicles);

	/**
	 * Tính toán lại route khi có delivery mới Input: Routes hiện tại, delivery
	 * mới Output: Routes đã được cập nhật
	 */
	List<Route> reoptimizeRoutes(List<Route> currentRoutes, Delivery newDelivery);

	/**
	 * Get current routes
	 * 
	 * @return List of current routes
	 */
	List<Route> getCurrentRoutes();
}