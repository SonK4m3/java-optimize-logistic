package sonnh.opt.opt_plan.repository.projection;

import sonnh.opt.opt_plan.constant.enums.RouteStatus;

public interface RouteStatusCount {
	RouteStatus getStatus();

	Long getCount();
}