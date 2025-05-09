package com.example.image_label.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.image_label.dao.UserDao;
import com.example.image_label.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author： LZF
 */
@Service
public class UserService extends ServiceImpl<UserDao, User> {

    @Autowired
    private UserDao UserDao;

    public List<User> getUserList(){
        //调用dao层,执行查询(所有数据)工作
        //userList--自定义变量
        List<User> userList=  UserDao.selectList(null);
        return userList;
    }
    //注册
    public boolean register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (getOne(queryWrapper) != null) {
            return false; // 用户名已存在
        }

        // 设置创建时间
        user.setCreatedAt(LocalDateTime.now());
        // 保存用户
        return save(user);
    }

    public User getUser(String uname, String pwd){
        QueryWrapper<User> user =  new QueryWrapper<>();
        // 修改列名
        user.eq("username", uname).eq("password", pwd);

        User relUser  = UserDao.selectOne(user);
        return  relUser;
    }

}