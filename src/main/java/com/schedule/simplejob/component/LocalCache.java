package com.schedule.simplejob.component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 本地缓存
 * @author: zhangbing
 * @create: 2020-09-03 10:42
 **/
public class LocalCache {

    private static volatile LocalCache cache = null;

    private LocalCache() {
    }

    public static LocalCache getInstance() {

        if (cache == null) {
            synchronized (LocalCache.class) {
                if (cache == null) {
                    cache = new LocalCache();
                }
            }
        }
        return cache;
    }

    private Object object = new Object();

    private Map<Object, Object> map = new ConcurrentHashMap<>();

    public Object addCache(Object t, Object v) {
        return map.put(t, v);
    }

    public boolean addCache(Object t) {
        return map.put(t, object) != null;
    }

    public Object getObject(Object t) {
        return map.get(t);
    }

    public Object remove(Object t) {
        return map.remove(t);
    }

    public Set<Map.Entry<Object, Object>> list() {
        return map.entrySet();
    }

    public boolean existKey(Object t) {
        return map.containsKey(t);
    }

}
