package com.hq.modules.operate.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hq.modules.operate.entity.ProblemLogging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @Description: 问题记录
 * @Author:
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Mapper
@Repository
public interface ProblemLoggingMapper extends BaseMapper<ProblemLogging> {

}
