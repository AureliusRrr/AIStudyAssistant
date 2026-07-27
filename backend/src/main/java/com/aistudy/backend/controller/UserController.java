package com.aistudy.backend.controller;

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
    public User create(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> list(){
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        userService.deleteUser(id);
        return "删除成功";
    }
}

















