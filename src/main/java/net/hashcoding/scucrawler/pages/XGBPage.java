package net.hashcoding.scucrawler.pages;

import net.hashcoding.scucrawler.HtmlEscapeFormatter;
import us.codecraft.webmagic.model.annotation.*;

import java.util.List;

/**
 * Created by Maochuan on 2016/10/26.
 */
@TargetUrl("http://xsc.scu.edu.cn/News_Detail.aspx*")
public class XGBPage implements BasePage {

    @ExtractByUrl(".*")
    String mUrl;

    @Formatter(formatter = HtmlEscapeFormatter.class)
    @ExtractBy(value="//span[@id='LbSubject']/allText()")
    String mTitle;

    @Formatter(formatter = HtmlEscapeFormatter.class)
    @ExtractBy(value="//div[@id='DivContent']/html()")
    String mContent;

    @ExtractBy(value="//div[@id='divattfile']//a/text()")
    List<String> mAttachmentName;

    @ExtractBy(value="//div[@id='divattfile']//a/@href")
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
