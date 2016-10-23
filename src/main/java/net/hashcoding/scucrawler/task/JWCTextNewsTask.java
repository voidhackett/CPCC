package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.Config;
import net.hashcoding.scucrawler.db.LeancloudDB;
import net.hashcoding.scucrawler.pages.JWCPage;
import net.hashcoding.scucrawler.pipeline.DefaultPageModelPipeline;
import net.hashcoding.scucrawler.pipeline.DumpPageModelPipeline;
import net.hashcoding.scucrawler.solver.HtmlToMarkdownSolver;
import net.hashcoding.scucrawler.solver.MarkdownToHtmlSolver;
import net.hashcoding.scucrawler.utils.Attachment;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Maochuan on 2016/10/23.
 */
public class JWCTextNewsTask extends PageTask {

    private Lock mStateLock;
    private boolean mLoginState;
    private LeancloudDB db;

    public JWCTextNewsTask() {
        registerPageSolver(new HtmlToMarkdownSolver());
        registerPageSolver(new MarkdownToHtmlSolver());

        db = new LeancloudDB();
        mLoginState = false;
        mStateLock = new ReentrantLock();
    }

    @Override
    public Spider createSpider() {
        Spider spider = OOSpider.create(
                Site.me().setSleepTime(1000).setCharset("utf-8"),
                new DefaultPageModelPipeline(),
                JWCPage.class);
        return spider.addUrl(Config.JWCTextNewsStarUrls);
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
    public boolean savePage(String url, String title,
                            String content, List<Attachment> attachments) {
        login();
        db.saveArticle(
                Config.TypeTextNews,
                url,
                title,
                content,
                attachments);
        return true;
    }

    @Override
    public String toString() {
        return "JWC text news task";
    }

    @Override
    protected void finalize() throws Throwable {
        logout();
        super.finalize();
    }
}
