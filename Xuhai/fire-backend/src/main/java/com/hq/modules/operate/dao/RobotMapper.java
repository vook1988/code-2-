package com.hq.modules.operate.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.Robot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RobotMapper extends BaseMapper<Robot> {
    List<Robot> selectRobotListWithType(Page<Robot> page, @Param("robot") Robot robot);
}