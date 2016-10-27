package net.hashcoding.scucrawler.pages;

import net.hashcoding.scucrawler.HtmlEscapeFormatter;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.Formatter;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by Maochuan on 2016/10/27.
 */
@TargetUrl(value = {
        "http://youth.scu.edu.cn/index.php/main/web/notice/detail/*",
        "http://youth.scu.edu.cn/index.php/main/web/news-group/detail/*",
        "http://youth.scu.edu.cn/index.php/main/web/courtyard-style/detail/*",
}, sourceRegion = "//ul[@class='list-art']//a")
public class QCCDPage implements BasePage {

    @ExtractByUrl(".*")
    String mUrl;

    @Formatter(formatter = HtmlEscapeFormatter.class)
    @ExtractBy(value="//div[@class='content-art']/h1/text()")
    String mTitle;

    @Formatter(formatter = HtmlEscapeFormatter.class)
    @ExtractBy(value="//div[@class='content-art']/div[@class='content-text']/html()")
    String mContent;

    // TODO:
    @ExtractBy(value="//body/table[4]//a/text()")
    List<String> mAttachmentName;

    // TODO:
    @ExtractBy(value="//body/table[4]//a/@href")
    List<String> mAttachmentUrl;

    String mThumbnail;

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String c) {
        mContent = c;
    }

    public void setThumbnail(String t) {
        mThumbnail = t;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public List<String> getAttachmentName() {
        return mAttachmentName;
    }

    public List<String> getAttachmentUrl() {
        return mAttachmentUrl;
    }
}
