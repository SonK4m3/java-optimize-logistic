package sonnh.opt.opt_plan.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Represents a request to create a new work shift. This class encapsulates the
 * necessary details for creating a work shift, including its name, start time,
 * end time, and active status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftCreateRequest {
	private String name; // The name of the work shift
	private LocalTime startTime; // The start time of the work shift
	private LocalTime endTime; // The end time of the work shift

	/**
	 * Validates the shift creation request.
	 * 
	 * @throws IllegalArgumentException if startTime is after endTime
	 */
	public void validate() {
		if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
			throw new IllegalArgumentException("Start time must be before end time.");
		}
	}
}
