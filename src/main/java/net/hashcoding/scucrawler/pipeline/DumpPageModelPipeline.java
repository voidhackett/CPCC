package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.Main;
import net.hashcoding.scucrawler.pages.BasePageImpl;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maochuan on 2016/9/23.
 */
public class DumpPageModelPipeline implements PageModelPipeline<BasePageImpl> {

    public void process(BasePageImpl page, Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append("\ndomain => ");
        builder.append(page.getUrl());
        builder.append("\n\n\ntitle => ");
        builder.append(page.getTitle());
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
}
