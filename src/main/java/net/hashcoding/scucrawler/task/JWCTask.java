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

public class JWCTask extends PageTask {

    private boolean mLoginState;
    private LeancloudDB db;

	public JWCTask() {
        registerPageSolver(new HtmlToMarkdownSolver());
        registerPageSolver(new MarkdownToHtmlSolver());

        db = new LeancloudDB();
        mLoginState = false;
    }

	@Override
	public Spider createSpider() {
		return OOSpider.create(Site.me().setSleepTime(1000).setCharset("utf-8"), 
				new JWCPageModelPipeline(), JWCPage.class)
			.addUrl(Config.JWCStartUrl);
	}

	public void login() {
		if (mLoginState)
            return;
        mLoginState = db.login(Config.JWCUsername, Config.JWCPassword);
	}

	public void logout() {
		if (!mLoginState)
            return;
        mLoginState = !db.logout();
	}

	public boolean loginState() {
		return mLoginState;
	}

	@Override
	public boolean isFetchedUrl(String url) {
        if (!loginState())
            login();
		return db.findUrl(url);
	}

	@Override
	public boolean savePage(String url, String title, String content) {
        if (!loginState())
            login();
		db.saveUrl(url);
        db.saveArticle(title, content);
		return true;
	}

	@Override
	public String toString() {
		return "JWCTask";
	}
}
