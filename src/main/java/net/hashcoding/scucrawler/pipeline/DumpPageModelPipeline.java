package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.Main;
import net.hashcoding.scucrawler.pages.BasePage;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maochuan on 2016/9/23.
 */
public class DumpPageModelPipeline implements PageModelPipeline<BasePage> {

    public void process(BasePage page, Task task) {
        String host = HtmlPreprocessor.getHostWithProtocol(
                task.getSite().getDomain());

        // Pretreatment
        HtmlPreprocessor.imagesAddHostAndGetThumbnail(page, host);
        HtmlPreprocessor.hyperlinksAddHost(page, host);
        HtmlPreprocessor.processOmitsAttachments(page);

        // dump
        StringBuilder builder = new StringBuilder();
        builder.append("\ndomain => ");
        builder.append(page.getUrl());
        dumpTitleAndThumbnail(builder, page);
        dumpContent(builder, page);
        dumpAttachment(builder, page);
        dumpToFile(builder.toString());
    }

    private void dumpTitleAndThumbnail(
            StringBuilder builder, BasePage page) {
        builder.append("\ntitle => ");
        builder.append(page.getTitle());
        builder.append("\nthumbnail => ");
        builder.append(page.getThumbnail());
    }

    private void dumpContent(StringBuilder builder, BasePage page) {
        builder.append("\ncontent => ");
        builder.append(page.getContent().substring(0, 150));
    }

    private void dumpAttachment(StringBuilder builder, BasePage page) {
        builder.append("\nattachment => {\n");
        List<String> names = page.getAttachmentName();
        List<String> urls = page.getAttachmentUrl();
        assert(names.size() == urls.size());
        Iterator<String> nameIt = names.iterator();
        Iterator<String> urlIt = urls.iterator();
        while (nameIt.hasNext() && urlIt.hasNext()) {
            builder.append("\r");
            builder.append(nameIt.next());
            builder.append(" => ");
            builder.append(urlIt.next());
            builder.append("\n");
        }
        builder.append("\n}\n\n");
    }

    private void dumpToFile(String content) {
        FileWriter writer;
        try {
            writer = new FileWriter(Main.dumpFilename, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
