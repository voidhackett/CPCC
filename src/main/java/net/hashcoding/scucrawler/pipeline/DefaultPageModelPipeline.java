package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.PageFactory;
import net.hashcoding.scucrawler.pages.BasePage;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * Created by Maochuan on 2016/10/17.
 */
public class DefaultPageModelPipeline
        implements PageModelPipeline<BasePage> {
    public void process(BasePage page, Task task) {
        String host = HtmlPreprocessor.getHostWithProtocol(
                task.getSite().getDomain());

        // Pretreatment
        HtmlPreprocessor.imagesAddHostAndGetThumbnail(page, host);
        HtmlPreprocessor.hyperlinksAddHost(page, host);
        HtmlPreprocessor.processOmitsAttachments(page);
        HtmlPreprocessor.attachmentAddHost(page, host);

        // Process
        PageFactory.instance().solve(page.getUrl(), page);
    }
}
