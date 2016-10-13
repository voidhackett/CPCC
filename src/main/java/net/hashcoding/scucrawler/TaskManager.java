package net.hashcoding.scucrawler;

import java.util.ArrayList;
import java.util.List;

import net.hashcoding.scucrawler.task.PageTask;

public class TaskManager {

    List<PageTask> tasks;

    public void delegateTask(PageTask task) {
        tasks.add(task);
    }

    public void run() {
        PageFactory factory = PageFactory.instance();
        System.out.println("Task manager start...");
        System.out.printf("[total]=%d\n", tasks.size());
        for (PageTask task : tasks) {
            System.out.printf("\tRunning task: %s\n", task.toString());
            factory.bindPageTask(task);
            task.createSpider().run();
        }
        System.out.println("Task manager stop.");
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
