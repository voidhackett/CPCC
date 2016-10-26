package net.hashcoding.scucrawler.db;


import com.avos.avoscloud.*;
import net.hashcoding.scucrawler.utils.Attachment;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.util.TextUtils;
import rx.Observable;
import rx.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maochuan on 2016/9/24.
 */
public class ArticleWrapper {

    // leancloud cloud function "String createArticle()"
    // return ID of article.
    public static Observable<String> create(final String type) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", type);
                    String objectId = AVCloud.callFunction("createArticle", params);
                    subscriber.onNext(objectId);
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<String> save(final String id,
                                          final String title,
                                          final String content) {
        return save(id, title, content, "");
    }

    // String saveTextArticle(id, title, content, thumbnail)
    // return id of article.
    public static Observable<String> save(final String id,
                                          final String title,
                                          final String content,
                                          final String thumbnail) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("objectId", id);
                    params.put("title", title);
                    params.put("content", content);
                    if (!TextUtils.isEmpty(thumbnail)) {
                        // TODO: image thumbnail
//                        String filename = "cover" + thumbnail.substring(thumbnail.lastIndexOf('.'));
//                        AVFile file = new AVFile(filename, thumbnail, new HashMap<String, Object>());
//                        file.save();
//                        String coverUrl = file.getThumbnailUrl(true, 100, 100);
                        params.put("thumbnail", thumbnail);
                    } else {
                        params.put("thumbnail", "");
                    }
                    String objectId = AVCloud.callFunction("saveTextArticle", params);
                    subscriber.onNext(objectId);
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<String> addAttachments(final String objectId,
                                                   final List<Attachment> attachments) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    if (attachments.isEmpty()) {
                        subscriber.onNext(objectId);
                        subscriber.onCompleted();
                        return;
                    }

                    AVObject article = AVObject.createWithoutData("Article", objectId);
                    AVRelation<AVObject> attachs = article.getRelation("attachments");
                    for (Attachment attachment : attachments) {
                        AVFile file = new AVFile(attachment.name, attachment.url, new HashedMap());
                        file.save();
                        attachs.add(AVObject.createWithoutData("_File", file.getObjectId()));
                    }
                    article.save();
                    subscriber.onNext(objectId);
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<AVFile> uploadFile(final String filename, final String url) {
        return Observable.create(new Observable.OnSubscribe<AVFile>() {
            public void call(final Subscriber<? super AVFile> subscriber) {
                try {
                    AVFile file = new AVFile(filename, url, new HashedMap());
                    file.save();
                    subscriber.onNext(file);
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    // int publish(id);
    //
    public static Observable<Integer> publish(final String id) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("objectId", id);
                    Integer integer = AVCloud.callFunction("publishArticle", params);
                    subscriber.onNext(integer);
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<String> saveUrl(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                try {
                    AVObject object = new AVObject("Urls");
                    object.put("url", url);
                    object.save();
                    subscriber.onNext(object.getObjectId());
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
