package net.hashcoding.scucrawler.pages;

import java.util.List;

public interface BasePage {
	String getUrl();
	String getTitle();
	String getContent();
    String getThumbnail();
	void setContent(String c);
    void setThumbnail(String t);
	List<String> getAttachmentName();
	List<String> getAttachmentUrl();
}
