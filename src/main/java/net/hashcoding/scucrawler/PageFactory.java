package net.hashcoding.scucrawler;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import net.hashcoding.scucrawler.pages.BasePageImpl;
import net.hashcoding.scucrawler.solver.PageSolver;
import net.hashcoding.scucrawler.task.PageTask;
import net.hashcoding.scucrawler.utils.MessageQueue;
import us.codecraft.webmagic.thread.CountableThreadPool;

public class PageFactory implements Runnable {

	PageTask mTask;

	private MessageQueue<FactoryRawData> mMessageQueue;
    private CountableThreadPool mTheadPool;

	public void solve(String url, BasePageImpl page) {
		assert(mTask != null);
        mMessageQueue.push(new FactoryRawData(mTask, url,
                page.getTitle(), page.getContent()));
	}

    public void run() {
        while (true) {
            FactoryRawData article = mMessageQueue.pop();
            if (article == null) {
                // TODO:
            } else {
                mTheadPool.execute(new Employee(
                        article.getPageTask(), article.getUrl(),
                        article.getTitle(), article.getContent()));
            }
        }
    }

    public void bindPageTask(PageTask task) {
		mTask = task;
	}

	public void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }
	
	private PageFactory() {
        mMessageQueue = new MessageQueue<FactoryRawData>();
        mTheadPool = new CountableThreadPool(Main.FactoryEmployeeSize);
    }

    private static class LazyHolder {
		private static final PageFactory INSTANCE = new PageFactory();
	}

	private static class Employee implements Runnable {

        PageTask mTask;
        String mUIID;
        String mTitle;
        String mContent;

        public Employee(PageTask task, String url, String t, String c) {
            mTask = task;
            mUIID = url;
            mTitle = t;
            mContent = c;
        }

        public void run() {
            if (mTask.isFetchedUrl(mUIID)) {
                return;
            }

            String content = mContent;

            List<PageSolver> solvers = mTask.getPageSolver();
            for (PageSolver sol : solvers) {
                content = sol.solve(content);
            }

            mTask.savePage(mUIID, mTitle, content);
        }
    }

	public static PageFactory instance() {
		return LazyHolder.INSTANCE;
	}
}
