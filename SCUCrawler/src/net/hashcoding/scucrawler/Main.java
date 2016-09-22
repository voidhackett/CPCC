package net.hashcoding.scucrawler;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import net.hashcoding.scucrawler.pages.JWCPage;

public class Main {

	public static void main(String[] args) {
		OOSpider.create(Site.me().setSleepTime(1000).setCharset("utf-8"), 
				new JWCPageModelPipeline(), JWCPage.class)
			.addUrl("http://jwc.scu.edu.cn/jwc/moreNotice.action")
			.run();
	}
}
