package net.hashcoding.scucrawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.hashcoding.scucrawler.pages.JWCPage;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

public class JWCPageModelPipeline implements PageModelPipeline<JWCPage> {

	@Override
	public void process(JWCPage page, Task task) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("title => ");
		builder.append(page.getTitle());
		builder.append("\ncontent => ");
		builder.append(page.getContent());
		builder.append("\n\nattachment => {\n");
		builder.append("title => ");

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
			writer = new FileWriter("D:\\test.txt", true);
			writer.write(builder.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
