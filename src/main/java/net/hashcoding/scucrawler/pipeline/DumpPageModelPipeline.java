package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.Main;
import net.hashcoding.scucrawler.PageFactory;
import net.hashcoding.scucrawler.pages.BasePageImpl;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maochuan on 2016/9/23.
 */
public class DumpPageModelPipeline implements PageModelPipeline<BasePageImpl> {

    public void process(BasePageImpl page, Task task) {
        String thumbnail = imagesAddHost(page, task.getSite().getDomain());

        StringBuilder builder = new StringBuilder();
        builder.append("\ndomain => ");
        builder.append(page.getUrl());
        builder.append("\n\n\ntitle => ");
        builder.append(page.getTitle());
        builder.append("\bthumbnail => ");
        builder.append(thumbnail);
        builder.append("\ncontent => ");
        builder.append(page.getContent());
        builder.append("\n\nattachment => {\n");
        List<String> names = page.getAttachmentName();
        List<String> urls = page.getAttachmentUrl();
        assert(names.size() == urls.size());
        Iterator<String> nameit = names.iterator();
        Iterator<String> urlit = urls.iterator();
        while (nameit.hasNext() && urlit.hasNext()) {
            builder.append("\r");
            builder.append(nameit.next());
            builder.append(" => ");
            builder.append(urlit.next());
            builder.append("\n");
        }
        builder.append("\n}\n");

        FileWriter writer;
        try {
            writer = new FileWriter(Main.dumpFilename, true);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String imagesAddHost(BasePageImpl page, String host) {
        String content = page.getContent();
        final String []thumbnail = {""};
        Document root = Jsoup.parse(content);
        root.setBaseUri(host);
        Elements images = root.select("img");
        images.forEach((Element element) -> {
            String url = host + element.attr("src");
            element.attr("src", url);
            if (TextUtils.isEmpty(thumbnail[0]))
                thumbnail[0] = url;
        });
        content = root.toString();
        page.setContent(content);
        return thumbnail[0];
    }
}
