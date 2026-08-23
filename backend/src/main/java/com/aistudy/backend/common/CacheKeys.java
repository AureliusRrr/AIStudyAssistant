package com.aistudy.backend.common;

public final class CacheKeys {
    private CacheKeys(){

    }
    public static final long USER_CACHE_TTL = 30 * 60;

    public static String user(Long userId){
        return "user:" + userId;
    }


}
