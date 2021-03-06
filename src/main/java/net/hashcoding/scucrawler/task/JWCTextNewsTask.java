package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.Config;
import net.hashcoding.scucrawler.pages.JWCPage;
import net.hashcoding.scucrawler.pipeline.DefaultPageModelPipeline;
import net.hashcoding.scucrawler.solver.HtmlToMarkdownSolver;
import net.hashcoding.scucrawler.solver.MarkdownToHtmlSolver;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

/**
 * Created by Maochuan on 2016/10/23.
 */
public class JWCTextNewsTask extends LeancloudBaseTask {

    public JWCTextNewsTask() {
        super(Config.TypeTextNews,
                Config.JWCUsername, Config.JWCPassword);
        registerPageSolver(new HtmlToMarkdownSolver());
        registerPageSolver(new MarkdownToHtmlSolver());
    }

    @Override
    public Spider createSpider() {
        Spider spider = OOSpider.create(
                Site.me().setSleepTime(1000).setCharset("utf-8"),
                new DefaultPageModelPipeline(),
                JWCPage.class);
        return spider.addUrl(Config.JWCTextNewsStarUrls);
    }

    @Override
    public String toString() {
        return "JWC text news task";
    }
}
