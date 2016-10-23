package net.hashcoding.scucrawler;

import java.util.ArrayList;
import java.util.List;

import net.hashcoding.scucrawler.task.PageTask;
import us.codecraft.webmagic.Spider;

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
            Spider spider = task.createSpider();
            spider.setEmptySleepTime(2000);
            spider.thread(5);
            spider.run();
            assert(spider.getStatus() == Spider.Status.Stopped);
            spider.close();
            System.out.printf("\tSpider of %s is done, dispatched to Factory\n", task.toString());
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
