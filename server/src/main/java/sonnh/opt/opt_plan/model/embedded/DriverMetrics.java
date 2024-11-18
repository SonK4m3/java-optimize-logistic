package sonnh.opt.opt_plan.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverMetrics {
    // Financial metrics
    private Double profitPerHour;
    private Double profitPerKm;
    private Double costRevenueRatio;
    
    // Performance metrics
    private Integer completedOrdersToday;
    private Double onTimeDeliveryRate;
    private Double averageRating;
    
    // Efficiency metrics
    private Double fuelEfficiency;  // km/liter
    private Double averageSpeedKmh;
    private Integer breakMinutesToday;
    
    // Earnings
    private Double todayEarnings;
    private Double weeklyEarnings;
    private Double monthlyEarnings;
    
    // Additional metrics
    private Double preferredZoneDeliveryRate;
    private Double highValueOrderRate;
    private Integer consecutiveOnTimeDeliveries;
} 