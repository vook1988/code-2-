package com.hq.modules.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hq.modules.operate.entity.Route;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 路线管理
 * @Author:
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Mapper
@Repository
public interface RouteMapper extends BaseMapper<Route> {

}
