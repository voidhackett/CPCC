package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.Config;
import net.hashcoding.scucrawler.pages.XGBPage;
import net.hashcoding.scucrawler.pipeline.DefaultPageModelPipeline;
import net.hashcoding.scucrawler.solver.HtmlToMarkdownSolver;
import net.hashcoding.scucrawler.solver.MarkdownToHtmlSolver;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

/**
 * Created by Maochuan on 2016/10/26.
 */
public class XGBAnnounceTask extends LeancloudBaseTask {

    public XGBAnnounceTask() {
        super(Config.TypeAnnouncement,
                Config.XGBUsername, Config.XGBPassword);
        registerPageSolver(new HtmlToMarkdownSolver());
        registerPageSolver(new MarkdownToHtmlSolver());
    }

    @Override
    public Spider createSpider() {
        Spider spider = OOSpider.create(
                Site.me().setSleepTime(1000).setCharset("utf-8"),
                new DefaultPageModelPipeline(),
                XGBPage.class);
        return spider.addUrl(Config.XGBAnnounceStartUrls);
    }

    @Override
    public String toString() {
        return "XGB announcement task";
    }
}
