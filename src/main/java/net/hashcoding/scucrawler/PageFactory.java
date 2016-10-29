package net.hashcoding.scucrawler;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.hashcoding.scucrawler.model.FactoryRawData;
import net.hashcoding.scucrawler.pages.BasePage;
import net.hashcoding.scucrawler.solver.PageSolver;
import net.hashcoding.scucrawler.task.PageTask;
import net.hashcoding.scucrawler.model.Attachment;
import us.codecraft.webmagic.thread.CountableThreadPool;

public class PageFactory implements Runnable {

	private PageTask mTask;
    private boolean mStop;
    private long mEmptySleepTime;

    private Thread mFactoryThread;
	private BlockingQueue<FactoryRawData> mMessageQueue;
    private CountableThreadPool mThreadPool;
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
            if (mThreadPool.getThreadAlive() == 0 && mStop)
                return ;
            mNewGoodsCondition.await(mEmptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            mNewGoodsLock.unlock();
        }
    }

	public void solve(String url, BasePage page) {
		assert(mTask != null);
        FactoryRawData data = new FactoryRawData(
                mTask,
                url,
                page.getThumbnail(),
                page.getTitle(),
                page.getContent(),
                page.getAttachmentName(),
                page.getAttachmentUrl());

        while (!mMessageQueue.offer(data)) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        signalNewGoods();
	}

    public void run() {
        while (true) {
            FactoryRawData article = mMessageQueue.poll();
            if (article == null) {
                if (mThreadPool.getThreadAlive() == 0 && mStop)
                    break;
                waitNewGoods();
            } else {
                mThreadPool.execute(new Employee(
                        article.getPageTask(),
                        article.getUrl(),
                        article.getThumbnail(),
                        article.getTitle(),
                        article.getContent(),
                        article.getAttachments()));
            }
        }

        mThreadPool.shutdown();
        assert(mThreadPool.isShutdown());
    }

    public void bindPageTask(PageTask task) {
		mTask = task;
	}

	public void start() {
        System.out.println("Page factory start...");
        mFactoryThread.setDaemon(false);
        mFactoryThread.start();
    }

    private void stop() {
        mStop = true;
    }

    //
    // must call it in main thread.
    public void waitFactoryStop() {
        assert(Thread.currentThread() == Main.MainThread);

        System.out.println("Blocking main thread & wait factory thread stop...");
        try {
            stop();
            mFactoryThread.join();
            assert(!mFactoryThread.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Factory thread stop.");
    }
	
	private PageFactory() {
        mStop = false;
        mEmptySleepTime = 3000;
        mFactoryThread = new Thread(this);
        mMessageQueue = new LinkedBlockingQueue<>(1000);
        mThreadPool = new CountableThreadPool(Main.FactoryEmployeeSize);
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
        String mThumbnail;
        List<Attachment> attachments;

        private Employee(
                PageTask task,
                String url,
                String thumb,
                String t,
                String c,
                List<Attachment> a) {
            mTask = task;
            mUIID = url;
            mThumbnail = thumb;
            mTitle = t;
            mContent = c;
            attachments = a;
        }

        public void run() {
            if (mTask.isFetchedUrl(mUIID))
                return;

            String content = mContent;

            List<PageSolver> solvers = mTask.getPageSolver();
            for (PageSolver sol : solvers) {
                content = sol.solve(content);
            }

            mTask.savePage(mUIID, mThumbnail, mTitle, content, attachments);
        }
    }

	public static PageFactory instance() {
		return LazyHolder.INSTANCE;
	}
}
