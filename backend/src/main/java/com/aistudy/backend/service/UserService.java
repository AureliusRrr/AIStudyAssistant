package com.aistudy.backend.service;

import com.aistudy.backend.dto.LoginRequest;
import com.aistudy.backend.dto.LoginResponse;
import com.aistudy.backend.dto.RegisterRequest;
import com.aistudy.backend.entity.User;

public interface UserService {
    User register(RegisterRequest Request);
    LoginResponse login(LoginRequest Request);
    User getById(Long id);
    User getByUsername(String username);

}