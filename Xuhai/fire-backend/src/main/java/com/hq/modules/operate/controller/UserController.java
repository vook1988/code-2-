package com.hq.modules.operate.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.User;
import com.hq.modules.operate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 创建用户
    @PostMapping
    public Map<String, Object> createUser(@RequestBody User user) {
        boolean result = userService.insert(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("data", result ? user : null);
        return response;
    }

    // 根据ID获取用户
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", user != null);
        response.put("data", user);
        return response;
    }

    // 更新用户
    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        boolean result = userService.updateById(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("data", result ? user : null);
        return response;
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Integer id) {
        boolean result = userService.deleteById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }

    // 分页查询用户列表
    @GetMapping
    public Map<String, Object> getUserList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer status) {

        EntityWrapper<User> wrapper = new EntityWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like("username", username);
        }
        if (email != null && !email.isEmpty()) {
            wrapper.like("email", email);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }

        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> resultPage = userService.selectPage(page, wrapper);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", resultPage.getRecords());
        response.put("total", resultPage.getTotal());
        response.put("pages", resultPage.getPages());
        response.put("current", resultPage.getCurrent());
        response.put("size", resultPage.getSize());
        return response;
    }

    // 根据用户名查询用户
    @GetMapping("/username/{username}")
    public Map<String, Object> getUserByUsername(@PathVariable String username) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("username", username);
        User user = userService.selectOne(wrapper);

        Map<String, Object> response = new HashMap<>();
        response.put("success", user != null);
        response.put("data", user);
        return response;
    }

    // 根据邮箱查询用户
    @GetMapping("/email/{email}")
    public Map<String, Object> getUserByEmail(@PathVariable String email) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("email", email);
        User user = userService.selectOne(wrapper);

        Map<String, Object> response = new HashMap<>();
        response.put("success", user != null);
        response.put("data", user);
        return response;
    }
}