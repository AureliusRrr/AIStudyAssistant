package com.aistudy.backend.controller;

import com.aistudy.backend.common.Result;
import com.aistudy.backend.service.RedisTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
public class RedisTestController {
    private final RedisTestService redisTestService;

    /*
    * GET /api/redis/tset
    * 写入test:hello 并读出来返回, 用于验证spring boot 与redis 连通
    */
    @GetMapping("/test")
    public Result<String> test(@RequestParam(defaultValue = "hello") String value){
        String key = "test:hello";
        redisTestService.setTestValue(key,value);
        String cached = redisTestService.getTestValue(key);
        return Result.success(cached);

    }
}
