package net.hashcoding.scucrawler.db;

import net.hashcoding.scucrawler.utils.Attachment;

import java.util.List;

/**
 * Created by Maochuan on 2016/9/23.
 */
public interface BaseDBImpl {

    boolean findUrl(String url);
    boolean removeUrl(String url);

    void saveArticle(String type, String url, String title, String Content, List<Attachment> attachments);
}
