package com.aistudy.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisCacheService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheService(StringRedisTemplate redisTemplate,ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void set(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key,json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("缓存序列化失败: " + key);
        }
    }

    public void set(String key,Object value,long ttlSeconds){
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key,json, Duration.ofSeconds(ttlSeconds));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("缓存序列化失败: " + key);
        }
    }


    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public String getAndExpire(String key,long ttlSeconds){
        String value = redisTemplate.opsForValue().get(key);
        if(value != null){
            redisTemplate.expire(key, Duration.ofSeconds(ttlSeconds));
        }
        return value;
    }

    public void setEmpty(String key,long ttlSeconds){
        redisTemplate.opsForValue().set(key,"",Duration.ofSeconds(ttlSeconds));
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
