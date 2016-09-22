package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.pages.JWCPage;
import net.hashcoding.scucrawler.pipeline.JWCPageModelPipeline;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

public class JWCTask extends PageTask {

	@Override
	public Spider createSpider() {
		return OOSpider.create(Site.me().setSleepTime(1000).setCharset("utf-8"), 
				new JWCPageModelPipeline(), JWCPage.class)
			.addUrl("http://jwc.scu.edu.cn/jwc/moreNotice.action");
	}

	@Override
	public void login() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean loginState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFetchedUrl(String domain) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean savePage(String domain, String title, String content) {
		// TODO Auto-generated method stub
		return false;
	}

}
