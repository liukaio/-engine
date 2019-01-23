package com.zyctd.wxlogin.cache;

public interface CacheService {

    void set(String key, String value);

    String get(String key);

    boolean expire(String key, long expire);

    void delete(String key);
}
