package net.hashcoding.scucrawler.pages;

import java.util.List;

public interface BasePageImpl {
	public abstract String getUrl();
	public abstract String getTitle();
	public abstract String getContent();
	public abstract List<String> getAttachmentName();
	public abstract List<String> getAttachmentUrl();
}
