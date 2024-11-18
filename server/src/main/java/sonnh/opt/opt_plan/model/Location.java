package sonnh.opt.opt_plan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
	private String address;
	private String ward;
	private String district;
	private String province;
	private double latitude;
	private double longitude;
}
