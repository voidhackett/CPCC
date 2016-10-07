package net.hashcoding.scucrawler.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maochuan on 2016/9/23.
 */
public class MessageQueue<T> {

    private List<T> queueCache = new LinkedList<T>();

    public T pop() {
        synchronized (queueCache) {
            int size = queueCache.size();
            if (size == 0) {
                return null;
            }
            return queueCache.remove(0);
        }
    }

    public void push(T element) {
        synchronized (queueCache) {
            Integer offerMaxQueue = 2000;
            if (queueCache.size() >= offerMaxQueue) {
                // throw new Exception("");
                // todo:
            }
            queueCache.add(element);
        }
    }
}
