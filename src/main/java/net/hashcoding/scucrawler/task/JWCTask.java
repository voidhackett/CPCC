package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.Config;
import net.hashcoding.scucrawler.db.LeancloudDB;
import net.hashcoding.scucrawler.pages.JWCPage;
import net.hashcoding.scucrawler.pipeline.JWCPageModelPipeline;
import net.hashcoding.scucrawler.solver.HtmlToMarkdownSolver;
import net.hashcoding.scucrawler.solver.MarkdownToHtmlSolver;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JWCTask extends PageTask {

    private Lock mStateLock;
    private boolean mLoginState;
    private LeancloudDB db;

	public JWCTask() {
        registerPageSolver(new HtmlToMarkdownSolver());
        registerPageSolver(new MarkdownToHtmlSolver());

        db = new LeancloudDB();
        mLoginState = false;
        mStateLock = new ReentrantLock();
    }

	@Override
	public Spider createSpider() {
		return OOSpider.create(Site.me().setSleepTime(1000).setCharset("utf-8"), 
				new JWCPageModelPipeline(), JWCPage.class)
			.addUrl(Config.JWCStartUrl).thread(5);
	}

	private void login() {
		if (loginState())
            return;
        mStateLock.lock();
        if (!loginState())
            mLoginState = db.login(Config.JWCUsername, Config.JWCPassword);
        mStateLock.unlock();
	}

	public void logout() {
		if (!loginState())
            return;
        mStateLock.lock();
        if (loginState())
            mLoginState = !db.logout();
        mStateLock.unlock();
	}

    private boolean loginState() {
		return mLoginState;
	}

	@Override
	public boolean isFetchedUrl(String url) {
        login();
		return db.findUrl(url);
	}

	@Override
	public boolean savePage(String url, String title, String content) {
        login();
        // 只有文章保存成功后才将 url 入库，避免潜在错误
        db.saveArticle(title, content);
        db.saveUrl(url);
		return true;
	}

	@Override
	public String toString() {
		return "JWCTask";
	}

	@Override
    protected void finalize() throws Throwable {
        logout();
        super.finalize();
    }
}
