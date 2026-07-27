package com.aistudy.backend.service.Impl;

import com.aistudy.backend.entity.User;
import com.aistudy.backend.mapper.UserMapper;
import com.aistudy.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public User updateUser(Long id, User user) {
        user.setId(id);
        userMapper.updateById(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
}
