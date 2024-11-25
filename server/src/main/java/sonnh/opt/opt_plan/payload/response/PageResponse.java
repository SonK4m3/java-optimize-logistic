package sonnh.opt.opt_plan.payload.response;

import org.springframework.data.domain.Page;
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

	public static <T> PageResponse<T> of(Page<T> page) {
		return PageResponse.<T> builder().docs(page.getContent()).page(page.getNumber())
				.limit(page.getSize()).totalDocs(page.getTotalElements())
				.totalPages(page.getTotalPages()).hasNextPage(page.hasNext())
				.hasPrevPage(page.hasPrevious()).build();
	}
}