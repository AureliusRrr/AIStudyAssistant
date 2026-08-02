package com.aistudy.backend.controller;

import com.aistudy.backend.common.Result;
import com.aistudy.backend.dto.LoginRequest;
import com.aistudy.backend.dto.LoginResponse;
import com.aistudy.backend.dto.RegisterRequest;
import com.aistudy.backend.entity.User;
import com.aistudy.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //注册
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request){
        User user = userService.register(request);
        //返回时隐藏密码
        user.setPassword(null);
        return Result.success(user);
    }

    //登录
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }

    //获取用户信息(需要登录)
    @GetMapping("/me")
    public Result<User> getCurrentUser(){
        //从SecurityContext取出userId (在JwtAuthenticationFilter中存入)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        User user = userService.getById(userId);
        //返回时隐藏密码
        user.setPassword(null);
        return Result.success(user);
    }
}

















