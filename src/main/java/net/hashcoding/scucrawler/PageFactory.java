package net.hashcoding.scucrawler;

import java.util.List;

import net.hashcoding.scucrawler.pages.BasePageImpl;
import net.hashcoding.scucrawler.solver.PageSolver;
import net.hashcoding.scucrawler.task.PageTask;

public class PageFactory {

	static PageFactory factory;
	
	PageTask mTask;

	public void solve(String domain, BasePageImpl page) {
		assert(mTask != null);
		if (mTask.isFetchedUrl(domain)) {
			return;
		}
		
		String content = page.getContent();
		
		List<PageSolver> solvers = mTask.getPageSolver();
		for (PageSolver sol : solvers) {
			content = sol.solve(content);
		}
		
		mTask.savePage(domain, page.getTitle(), page.getContent());
	}
	
	public void bindPageTask(PageTask task) {
		mTask = task;
	}
	
	
	private PageFactory() { }
	
	private static class LazyHolder {
		private static final PageFactory INSTANCE = new PageFactory();
	}

	public static PageFactory instance() {
		return LazyHolder.INSTANCE;
	}
}
