package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.PageFactory;
import net.hashcoding.scucrawler.pages.JWCPage;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

public class JWCPageModelPipeline implements PageModelPipeline<JWCPage> {

	public void process(JWCPage page, Task task) {
		PageFactory.instance().solve(page.getUrl(), page);
	}
}
