package com.aistudy.backend.service.Impl;

import com.aistudy.backend.common.JwtUtils;
import com.aistudy.backend.dto.LoginRequest;
import com.aistudy.backend.dto.LoginResponse;
import com.aistudy.backend.dto.RegisterRequest;
import com.aistudy.backend.entity.User;
import com.aistudy.backend.mapper.UserMapper;
import com.aistudy.backend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public User register(RegisterRequest request) {
        //1.检查用户名是否存在
        LambdaQueryWrapper<User> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if(userMapper.selectCount(wrapper) > 0){
            throw new RuntimeException("�û����Ѵ���");
        }

        //2.创建用户,并密码加密
        User user = new User();
        user.setUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole("User");

        //3.保存用户
        userMapper.insert(user);
        return user;

    }

    @Override
    public LoginResponse login(LoginRequest Request) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }
}