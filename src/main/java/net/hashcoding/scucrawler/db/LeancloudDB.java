package net.hashcoding.scucrawler.db;

import com.avos.avoscloud.*;
import net.hashcoding.scucrawler.utils.Attachment;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
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

    public void saveArticle(final String type,
                            final String url,
                            final String thumbnail,
                            final String title,
                            final String content,
                            final List<Attachment> attachments) {
        ArticleWrapper.create(type)
                .flatMap(new Func1<String, Observable<? extends String>>() {
                    public Observable<? extends String> call(String s) {
                        return ArticleWrapper.save(s, title, content, thumbnail);
                    }
                })
                .flatMap(new Func1<String, Observable<? extends String>>() {
                    public Observable<? extends String> call(String s) {
                        return ArticleWrapper.addAttachments(s, attachments);
                    }
                })
                .flatMap(new Func1<String, Observable<? extends Integer>>() {
                    public Observable<? extends Integer> call(String s) {
                        return ArticleWrapper.publish(s);
                    }
                })
                .flatMap(new Func1<Integer, Observable<? extends String>>() {
                    public Observable<? extends String> call(Integer i) {
                        return ArticleWrapper.saveUrl(url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    public void call(String urlObjectId) {
                        System.out.printf("url [%s] done\n", url);
                    }
                }, new Action1<Throwable>() {
                    public void call(Throwable throwable) {
                        System.out.printf("url [%s] is failure\n", url);
                        throwable.printStackTrace();
                    }
                });
    }
}
