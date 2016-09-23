package net.hashcoding.scucrawler.task;

import java.util.ArrayList;
import java.util.List;

import net.hashcoding.scucrawler.solver.PageSolver;
import us.codecraft.webmagic.Spider;

public abstract class PageTask {
	
	List<PageSolver> librarys;
	
	public PageTask() {
		librarys = new ArrayList<PageSolver>();
	}

	//  按照注册顺序调用
	public void registerPageSolver(PageSolver solver) {
		librarys.add(solver);
	}
	
	public List<PageSolver> getPageSolver() {
		return librarys;
	}
	
	public abstract Spider createSpider();
	
	public abstract boolean isFetchedUrl(String domain);
	
	public abstract boolean savePage(String domain, String title, String content);
}
