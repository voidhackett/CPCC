package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.PageFactory;
import net.hashcoding.scucrawler.pages.BasePageImpl;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * Created by Maochuan on 2016/10/17.
 */
public class DefaultPageModelPipeline
        implements PageModelPipeline<BasePageImpl> {
    public void process(BasePageImpl page, Task task) {
        PageFactory.instance().solve(page.getUrl(), page);
    }
}
