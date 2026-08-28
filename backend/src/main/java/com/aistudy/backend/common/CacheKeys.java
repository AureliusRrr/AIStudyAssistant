package com.aistudy.backend.common;

public final class CacheKeys {
    private CacheKeys(){

    }

    //=====================TTL常量=====================

    public static final long USER_CACHE_TTL = 30 * 60;

    public static final long DOCUMENT_LIST_CACHE_TTL = 10 * 60;

    //=====================key模板方法=====================

    public static String user(Long userId){
        return "user:" + userId;
    }

    public static String documentList(Long userId){
        return "document:list:" + userId;
    }

    public static String noteList(Long userId,String keyword,String tag){
        return "note:list:" + userId + ":" + keyword + ":" + tag;
    }


}
