package net.hashcoding.scucrawler.pages;

import java.util.List;

import net.hashcoding.scucrawler.HtmlEscapeFormatter;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.Formatter;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://jwc.scu.edu.cn/jwc/newsShow.action*")
@HelpUrl("http://jwc.scu.edu.cn/jwc/moreNotice.action")
public class JWCPage implements BasePageImpl {

	@ExtractByUrl(".*")
	String mUrl;
	
	@Formatter(formatter = HtmlEscapeFormatter.class)
	@ExtractBy(value="//body/table[3]/tbody/tr/tb/b")
	String mTitle;

	@Formatter(formatter = HtmlEscapeFormatter.class)
	@ExtractBy(value="123123//input[@id='news_content']/@value-ddd")
	String mContent;

	@ExtractBy(value="//body/table[4]/a/text()")
	List<String> mAttachmentName;
	
	@ExtractBy(value="//body/table[4]/a/@href")
	List<String> mAttachmentUrl;

	@Override
	public String getUrl() {
		return mUrl;
	}
	
	@Override
	public String getTitle() {
		return mTitle;
	}
	
	@Override
	public String getContent() {
		return mContent;
	}
	
	@Override
	public List<String> getAttachmentName() {
		return mAttachmentName;
	}
	
	@Override
	public List<String> getAttachmentUrl() {
		return mAttachmentUrl;
	}
}
