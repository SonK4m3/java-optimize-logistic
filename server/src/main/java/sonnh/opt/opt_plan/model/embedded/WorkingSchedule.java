package sonnh.opt.opt_plan.model.embedded;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Set;

@Embeddable
@Data
public class WorkingSchedule {
	private LocalTime preferredStartTime;
	private LocalTime preferredEndTime;
	private Integer maxWorkingHours;
	private Integer maxOrdersPerDay;
	private Integer minBreakMinutes;
	private Integer maxActiveOrders;

	@ElementCollection
	private Set<DayOfWeek> workingDays;

	public boolean canAcceptNewOrder(int currentOrders, LocalDateTime now) {
		return currentOrders < maxOrdersPerDay && isWithinWorkingHours(now)
				&& workingDays.contains(now.getDayOfWeek());
	}

	private boolean isWithinWorkingHours(LocalDateTime now) {
		LocalTime currentTime = now.toLocalTime();
		return currentTime.isAfter(preferredStartTime)
				&& currentTime.isBefore(preferredEndTime);
	}
}