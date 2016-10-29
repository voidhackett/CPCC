package net.hashcoding.scucrawler.pipeline;

import net.hashcoding.scucrawler.pages.BasePage;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    public static void processOmitsAttachments(BasePage page) {
        // TODO:
    }
}
