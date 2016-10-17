package net.hashcoding.scucrawler.db;

import com.avos.avoscloud.*;
import net.hashcoding.scucrawler.utils.Attachment;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.ArrayList;


/**
 * Created by Maochuan on 2016/9/23.
 *
 * NOTICE: login need.
 */
public class LeancloudDB implements BaseDBImpl {

    public boolean login(String username, String password) {
        try {
            AVUser.logIn(username, password);
        } catch (AVException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return true;
    }

    public boolean logout() {
        AVUser.logOut();
        return true;
    }

    public void saveUrl(String url) {
        AVObject object = new AVObject("Urls");
        object.put("url", url);
        try {
            object.save();
        } catch (AVException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // sync
    public boolean findUrl(String url) {
        AVQuery<AVObject> urls = new AVQuery<AVObject>("Urls");
        urls.whereEqualTo("url", url);
        try {
            return urls.find().size() != 0;
        } catch (AVException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeUrl(String url) {
        return false;
    }

    public void saveArticle(final String title,
                            final String content,
                            final List<Attachment> attachments) {

        Observable<String> createAsync = ArticleWrapper.create();
        Observable<String> saveAsync = createAsync.flatMap(
                new Func1<String, Observable<? extends String>>() {
                    public Observable<? extends String> call(String s) {
                        return ArticleWrapper.save(s, title, content);
                    }
                });

        // 如果值不为 0 中间加入上传操作
        if (attachments.isEmpty()) {
            for (final Attachment attachment : attachments) {
                saveAsync = saveAsync.flatMap(new Func1<String, Observable<? extends String>>() {
                            public Observable<String> call(String s) {
                        return ArticleWrapper.addAttachment(
                                s, attachment.name, attachment.url);
                        }
                    });
            }
        }

        Observable<Integer> publishAsync = saveAsync.flatMap(
                new Func1<String, Observable<? extends Integer>>() {
                    public Observable<Integer> call(String s) {
                        return ArticleWrapper.publish(s);
                    }
                });

        publishAsync.subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    public void onCompleted() {
                        // TODO: success
                    }

                    public void onError(Throwable throwable) {
                        // TODO: error
                        throwable.printStackTrace();
                        System.exit(-1);
                    }

                    public void onNext(Integer integer) {
                    }
                });
    }
}
