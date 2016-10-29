package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.pages.BasePage;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import java.util.List;

/**
 * Created by Maochuan on 2016/10/29.
 */
public class HtmlPreprocessor {
    public static String getHostWithProtocol(String originHost) {
        // absUrl need protocol of host
        if (!originHost.startsWith("http://")
                && !originHost.startsWith("HTTP://")) {
            originHost = "http://" + originHost;
        }
        return originHost;
    }

    public static void imagesAddHostAndGetThumbnail(
            BasePage page, String host) {
        final String[] thumbnail = {""};

        Document root = Jsoup.parse(page.getContent());
        root.setBaseUri(host);

        Elements images = root.select("img");
        images.forEach((Element element) -> {
            String url = element.absUrl("src");
            element.attr("src", url);
            if (TextUtils.isEmpty(thumbnail[0]))
                thumbnail[0] = url;
        });

        page.setContent(root.toString());
        page.setThumbnail(thumbnail[0]);
    }

    public static void hyperlinksAddHost(BasePage page, String host) {
        Document root = Jsoup.parse(page.getContent());
        root.setBaseUri(host);

        Elements images = root.select("a");
        images.forEach((Element element) ->
            element.attr("href", element.absUrl("href"))
        );

        page.setContent(root.toString());
    }

    public static void attachmentAddHost(BasePage page, String host) {
        // 这里 urls 内部实现为 ArrayList
        List<String> urls = page.getAttachmentUrl();
        int size = urls.size();
        for (int i = 0; i < size; ++i) {
            urls.set(i, StringUtil.resolve(host, urls.get(i)));
        }
    }

    public static void processOmitsAttachments(BasePage page) {
        // TODO: 等待大神
    }
}
