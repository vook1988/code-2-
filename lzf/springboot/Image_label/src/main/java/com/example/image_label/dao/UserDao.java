package com.example.image_label.dao;

import com.example.image_label.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author lzf
 * @since 2025-04-30
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}
