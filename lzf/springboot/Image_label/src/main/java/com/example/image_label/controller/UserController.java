package com.example.image_label.controller;

import com.example.image_label.pojo.User;
import com.example.image_label.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        if (user.getUsername() == null || user.getPassword() == null) {
            result.put("code", 400);
            result.put("msg", "用户名或密码不能为空");
            return result;
        }
        boolean success = userService.register(user);
        if (success) {
            result.put("code", 200);
            result.put("msg", "注册成功");
        } else {
            result.put("code", 401);
            result.put("msg", "用户名已存在");
        }
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || password == null) {
            result.put("code", 400);
            result.put("msg", "用户名或密码不能为空");
            return result;
        }

        // 调用 Service 验证登录
        User user = userService.getUser(username, password);
        if (user != null) {
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", user); // 返回用户信息（可根据需求调整返回字段）
        } else {
            result.put("code", 1);
            result.put("msg", "无用户");
        }
        return result;
    }


}