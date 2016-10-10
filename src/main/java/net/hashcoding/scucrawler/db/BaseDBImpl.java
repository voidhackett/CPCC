package net.hashcoding.scucrawler.db;

/**
 * Created by Maochuan on 2016/9/23.
 */
public interface BaseDBImpl {

    void saveUrl(String url);
    boolean findUrl(String url);
    boolean removeUrl(String url);

    void saveArticle(String title, String Content);
}
