package net.hashcoding.scucrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.hashcoding.scucrawler.task.PageTask;

public class TaskManager {

    List<PageTask> tasks;

    public void delegateTask(PageTask task) {
        tasks.add(task);
    }

    public void run() {
        PageFactory factory = PageFactory.instance();
        System.out.printf("Begin... total: %d\n\n", tasks.size());
        for (PageTask task : tasks) {
            System.out.printf("\tRunning: %s\n", task.toString());
            factory.bindPageTask(task);
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
