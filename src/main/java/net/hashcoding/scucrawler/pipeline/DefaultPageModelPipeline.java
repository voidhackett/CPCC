package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.PageFactory;
import net.hashcoding.scucrawler.pages.BasePage;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * Created by Maochuan on 2016/10/17.
 */
public class DefaultPageModelPipeline
        implements PageModelPipeline<BasePage> {
    public void process(BasePage page, Task task) {
        imagesAddHost(page, task.getSite().getDomain());
        PageFactory.instance().solve(page.getUrl(), page);
    }

    private void imagesAddHost(BasePage page, String host) {
        // absUrl need protocol of host
        if (!host.startsWith("http://") && !host.startsWith("HTTP://")) {
            host = "http://" + host;
        }
        String content = page.getContent();
        final String[] thumbnail = {""};
        Document root = Jsoup.parse(content);
        root.setBaseUri(host);
        Elements images = root.select("img");
        images.forEach((Element element) -> {
            String url = element.absUrl("src");
            element.attr("src", url);
            if (TextUtils.isEmpty(thumbnail[0]))
                thumbnail[0] = url;
        });
        content = root.toString();
        page.setContent(content);
        page.setThumbnail(thumbnail[0]);
    }
}
