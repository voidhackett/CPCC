package net.hashcoding.scucrawler.db;


import com.avos.avoscloud.*;
import org.apache.commons.collections.map.HashedMap;
import rx.Observable;
import rx.Subscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maochuan on 2016/9/24.
 */
public class ArticleWrapper {

    // leancloud cloud function "String createAnnouncement()"
    // return ID of article.
    public static Observable<String> create() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(final Subscriber<? super String> subscriber) {
                Map<String, String> params = new HashMap<String, String>();
                AVCloud.callFunctionInBackground("createAnnouncement",
                        params, new FunctionCallbackImpl<String>(subscriber));
            }
        });
    }

    // String saveAnnouncement(id, title, content)
    // return id of article.
    public static Observable<String> save(final String id,
                                    final String title, final String content) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("announce", id);
                params.put("title", title);
                params.put("content", content);
                AVCloud.callFunctionInBackground("saveAnnouncement",
                        params, new FunctionCallbackImpl<String>(subscriber));
            }
        });
    }

    public static Observable<String> addAttachment(final String objectId,
                                                   final String name,
                                                   final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(final Subscriber<? super String> subscriber) {
                // TODO: avcloud save attachments
                //  first: upload file
                //  second: add attachment
                //  return objectId
            }
        });
    }

    public static Observable<AVFile> uploadFile(final String filename, final String url) {
        return Observable.create(new Observable.OnSubscribe<AVFile>() {
            public void call(final Subscriber<? super AVFile> subscriber) {
                final AVFile file = new AVFile(filename, url, new HashedMap());
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(file);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });
    }

    // int publish(id);
    //
    public static Observable<Integer> publish(final String id) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            public void call(Subscriber<? super Integer> subscriber) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("announce", id);
                AVCloud.callFunctionInBackground("publishAnnouncement",
                        params, new FunctionCallbackImpl<Integer>(subscriber));
            }
        });
    }

    public static class FunctionCallbackImpl<T> extends FunctionCallback<T> {

        final Subscriber<? super T> subscriber;

        public FunctionCallbackImpl(final Subscriber<? super T> sub) {
            assert(sub != null);
            subscriber = sub;
        }

        public void done(T t, AVException e) {
            if (e == null) {
                subscriber.onNext(t);
                subscriber.onCompleted();
            } else {
                subscriber.onError(e);
            }
        }
    }
}
