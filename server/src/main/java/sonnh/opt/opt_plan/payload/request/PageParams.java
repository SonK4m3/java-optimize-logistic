package sonnh.opt.opt_plan.payload.request;

import lombok.Data;

@Data
public class PageParams {
	private int page = 0;
	private int limit = 10;
	private String sortBy = "createdAt";
	private String sortDir = "desc";
}