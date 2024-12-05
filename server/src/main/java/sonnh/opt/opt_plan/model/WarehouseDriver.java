package sonnh.opt.opt_plan.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDriver implements Cloneable {
    private Driver driver;
    private List<Long> warehouseIds;

    @Override
    public WarehouseDriver clone() {
        return WarehouseDriver.builder()
                .driver(driver.clone())
                .warehouseIds(warehouseIds)
                .build();
    }
}
