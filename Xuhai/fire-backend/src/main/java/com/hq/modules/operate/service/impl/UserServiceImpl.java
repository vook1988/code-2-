package com.hq.modules.operate.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hq.modules.operate.entity.User;
import com.hq.modules.operate.dao.UserMapper;
import com.hq.modules.operate.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}