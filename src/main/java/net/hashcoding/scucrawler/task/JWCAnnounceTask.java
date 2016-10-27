package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.Config;
import net.hashcoding.scucrawler.db.LeancloudDB;
import net.hashcoding.scucrawler.pages.JWCPage;
import net.hashcoding.scucrawler.pipeline.DefaultPageModelPipeline;
import net.hashcoding.scucrawler.solver.HtmlToMarkdownSolver;
import net.hashcoding.scucrawler.solver.MarkdownToHtmlSolver;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

public class JWCAnnounceTask extends LeancloudBaseTask {

	public JWCAnnounceTask() {
		super(Config.TypeAnnouncement,
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
		return spider.addUrl(Config.JWCAnnounceStartUrls);
	}

	@Override
	public String toString() {
		return "JWC announce task";
	}
}
