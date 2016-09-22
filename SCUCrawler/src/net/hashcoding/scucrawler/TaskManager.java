package net.hashcoding.scucrawler;

import java.util.ArrayList;
import java.util.List;

import net.hashcoding.scucrawler.task.PageTask;

public class TaskManager {
	
	List<PageTask> tasks;
	
	public void addTask(PageTask task) {
		tasks.add(task);
	}
	
	public void run() {
		for (PageTask task : tasks) {
			task.createSpider().run();
		}
	}
	
	private TaskManager() {
		tasks = new ArrayList<PageTask>();
	}
	
	private static class LazyHolder {
		private static final TaskManager INSTANCE = new TaskManager();
	}

	public static TaskManager instance() {
		return LazyHolder.INSTANCE;
	}
}
