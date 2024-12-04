package sonnh.opt.opt_plan.payload.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;

@Data
@Builder
public class SuggestDriversResponse {
	private List<WarehouseDriver> warehouseDrivers;

	@Data
	@Builder
	public static class WarehouseDriver {
		private DriverDTO driver;
		private List<Long> warehouseIds;
	}

	public void addDriver(DriverDTO driver, List<Long> warehouseIds) {
		if (warehouseDrivers == null) {
			warehouseDrivers = new ArrayList<>();
		}
		warehouseDrivers.add(WarehouseDriver.builder().driver(driver)
				.warehouseIds(warehouseIds).build());
	}
}
