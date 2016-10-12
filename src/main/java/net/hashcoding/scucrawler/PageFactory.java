package net.hashcoding.scucrawler;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import net.hashcoding.scucrawler.pages.BasePageImpl;
import net.hashcoding.scucrawler.solver.PageSolver;
import net.hashcoding.scucrawler.task.PageTask;
import net.hashcoding.scucrawler.utils.MessageQueue;
import us.codecraft.webmagic.thread.CountableThreadPool;

public class PageFactory implements Runnable {

	private PageTask mTask;
    private boolean mStop;

	private MessageQueue<FactoryRawData> mMessageQueue;
    private CountableThreadPool mTheadPool;
    private ReentrantLock mNewGoodsLock;
    private Condition mNewGoodsCondition;

    private void signalNewGoods() {
        try {
            mNewGoodsLock.lock();
            mNewGoodsCondition.signalAll();
        } finally {
            mNewGoodsLock.unlock();
        }
    }

    private void waitNewGoods() {
        mNewGoodsLock.lock();

        try {
            if (mTheadPool.getThreadAlive() == 0 && mStop)
                return ;
            mNewGoodsCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO:
            System.exit(-1);
        } finally {
            mNewGoodsLock.unlock();
        }
    }

	public void solve(String url, BasePageImpl page) {
		assert(mTask != null);
        mMessageQueue.push(new FactoryRawData(mTask, url,
                page.getTitle(), page.getContent()));
        signalNewGoods();
	}

    public void run() {
        while (true) {
            FactoryRawData article = mMessageQueue.pop();
            if (article == null) {
                if (mTheadPool.getThreadAlive() == 0 && mStop)
                    break;
                waitNewGoods();
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

    public void stop() {
        mStop = true;
    }
	
	private PageFactory() {
        mStop = false;
        mMessageQueue = new MessageQueue<FactoryRawData>();
        mTheadPool = new CountableThreadPool(Main.FactoryEmployeeSize);
        mNewGoodsLock = new ReentrantLock();
        mNewGoodsCondition = mNewGoodsLock.newCondition();
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
