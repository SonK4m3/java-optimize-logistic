package sonnh.opt.opt_plan.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
	private List<T> docs;
	private long totalDocs;
	private int limit;
	private int page;
	private int totalPages;
	private boolean hasNextPage;
	private boolean hasPrevPage;
}