

package com.hq.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hq.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @author xxx
 * @email sunlightcs@gmail.com
 *
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
