package net.hashcoding.scucrawler.db;

/**
 * Created by Maochuan on 2016/9/23.
 *
 * NOTICE: login need.
 */
public class LeancloudDB implements BaseDBImpl {

    public boolean login(String username, String password) {
        return false;
    }

    public boolean logout() {
        return true;
    }

    public void saveUrl(String url) {

    }

    public boolean findUrl(String url) {
        return false;
    }

    public boolean removeUrl(String url) {
        return false;
    }

    public void saveArticle(String title, String Content) {

    }
}
