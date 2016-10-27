package net.hashcoding.scucrawler.task;

import net.hashcoding.scucrawler.db.LeancloudDB;
import net.hashcoding.scucrawler.utils.Attachment;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Created by Maochuan on 2016/10/27.
 */
public abstract class LeancloudBaseTask extends PageTask {

    private Lock mStateLock;
    private boolean mLoginState;
    private LeancloudDB mDB;
    private String mArticleType;
    private String mUsername;
    private String mPassword;

    public LeancloudBaseTask(
            String type, String username, String password) {
        mDB = new LeancloudDB();
        mLoginState = false;
        mStateLock = new ReentrantLock();
        mArticleType = type;
        mUsername = username;
        mPassword = password;

        assert (username != null && password != null && type != null);
    }

    private void login() {
        if (loginState())
            return;
        mStateLock.lock();
        if (!loginState())
            mLoginState = mDB.login(mUsername, mPassword);
        mStateLock.unlock();
    }

    public void logout() {
        if (!loginState())
            return;
        mStateLock.lock();
        if (loginState())
            mLoginState = !mDB.logout();
        mStateLock.unlock();
    }

    private boolean loginState() {
        return mLoginState;
    }

    @Override
    public boolean isFetchedUrl(String url) {
        login();
        return mDB.findUrl(url);
    }

    @Override
    public boolean savePage(String url,
                            String thumb,
                            String title,
                            String content,
                            List<Attachment> attachments) {
        login();
        mDB.saveArticle(
                mArticleType,
                url,
                thumb,
                title,
                content,
                attachments);
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        logout();
        super.finalize();
    }
}
