package com.aistudy.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final StringRedisTemplate redisTemplate;

    /**
     * 写入一个key,并设置60s过期
     */
    public void setTestValue(String key,String value){
        redisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);
    }

    /**
     * 读取一个key
     */
    public String getTestValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除一个key,返回是否删除成功
     */
    public Boolean deleteTestValue(String key){
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }


}
