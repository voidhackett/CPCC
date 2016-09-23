package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.task.PageTask;
import us.codecraft.webmagic.Page;

/**
 * Created by Maochuan on 2016/9/23.
 */
public class FactoryRawData {

    String mUrl;
    String mTitle;
    String mContent;
    PageTask mTask;

    public FactoryRawData(PageTask task, String url, String t, String c) {
        mUrl = url;
        mTitle = t;
        mContent = c;
        mTask = task;
    }

    public String getUrl() {
        return mUrl;
    }

    public PageTask getPageTask() {
        return mTask;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }
}
