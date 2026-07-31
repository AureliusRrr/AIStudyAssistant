package com.aistudy.backend.controller;

import com.aistudy.backend.common.Result;
import com.aistudy.backend.entity.User;
import com.aistudy.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public Result<User> create(@RequestBody User user){
        return Result.success(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id){
        return Result.success(userService.getUserById(id));
    }

    @GetMapping
    public Result<List<User>> list(){
        return Result.success(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @RequestBody User user){
        return Result.success(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}

















