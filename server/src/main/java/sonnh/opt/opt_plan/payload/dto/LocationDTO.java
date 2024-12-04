package sonnh.opt.opt_plan.payload.dto;

import sonnh.opt.opt_plan.model.Location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDTO {
	private String address;
	private Double latitude;
	private Double longitude;

	public static LocationDTO fromEntity(Location location) {
		if (location == null)
			return null;
		return LocationDTO.builder().address(location.getAddress())
				.latitude(location.getLatitude()).longitude(location.getLongitude())
				.build();
	}
}
