package com.aistudy.backend.service.Impl;

import com.aistudy.backend.common.CacheKeys;
import com.aistudy.backend.common.JwtUtils;
import com.aistudy.backend.dto.LoginRequest;
import com.aistudy.backend.dto.LoginResponse;
import com.aistudy.backend.dto.RegisterRequest;
import com.aistudy.backend.entity.User;
import com.aistudy.backend.mapper.UserMapper;
import com.aistudy.backend.service.RedisCacheService;
import com.aistudy.backend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisCacheService redisCacheService;
    private final ObjectMapper objectMapper;

    @Override
    public User register(RegisterRequest request) {
        //1.检查用户名是否存在
        LambdaQueryWrapper<User> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if(userMapper.selectCount(wrapper) > 0){
            throw new RuntimeException("用户名已存在");
        }

        //2.创建用户,并密码加密
        User user = new User();
        user.setUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole("User");

        //3.保存用户
        userMapper.insert(user);

        redisCacheService.delete(CacheKeys.user(user.getId()));//删Redis缓存

        return user;

    }

    @Override
    public LoginResponse login(LoginRequest request) {
        //1.查用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);

        if(user == null){
            throw new RuntimeException("用户不存在");
        }

        //2.校验密码
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("用户名或密码错误");
        }

        //3.生成JWT
        String token = jwtUtils.generateToken(user.getId(),user.getUsername());
        return new LoginResponse(token,user.getId(),user.getUsername());
    }

    @Override
    public User getById(Long id) {
        //1.先查Redis
        String cached = redisCacheService.get(CacheKeys.user(id));
        if(cached != null){
            //2.命中:把JSON反序列化为User返回,不再查找数据库
            try{
                return objectMapper.readValue(cached,User.class);
            } catch (JsonProcessingException e){
                log.warn("用户缓存反序列化失败:{}",id,e);
            }

        }

        //3.未命中:查MySQL
        User user = userMapper.selectById(id);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }

        //4.写回Redis(不缓存密码)
        user.setPassword(null);
        redisCacheService.set(CacheKeys.user(id),user,CacheKeys.USER_CACHE_TTL);

        return user;
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }
}