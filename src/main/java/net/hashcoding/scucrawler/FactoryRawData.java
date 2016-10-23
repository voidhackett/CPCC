package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.task.PageTask;
import net.hashcoding.scucrawler.utils.Attachment;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Maochuan on 2016/9/23.
 */
public class FactoryRawData {

    String mUrl;
    String mTitle;
    String mContent;
    String mThumbnail;
    List<Attachment> attachmentList;
    PageTask mTask;

    public FactoryRawData(
            PageTask task,
            String url,
            String thumb,
            String t,
            String c,
            List<String> filename,
            List<String> urls) {
        mUrl = url;
        mThumbnail = thumb;
        mTitle = t;
        mContent = c;
        mTask = task;

        attachmentList = new ArrayList<Attachment>();
        assert (filename.size() == urls.size());
        int length = filename.size();
        for (int i = 0; i < length; ++i) {
            attachmentList.add(new Attachment(filename.get(i), urls.get(i)));
        }
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

    public String getThumbnail() {
        return mThumbnail;
    }

    public List<Attachment> getAttachments() {
        return attachmentList;
    }
}
