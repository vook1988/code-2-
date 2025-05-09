package com.hq.modules.operate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hq.modules.operate.entity.PatrolPlan;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**

 */
@Mapper
@Repository
public interface PatrolPlanMapper extends BaseMapper<PatrolPlan> {

}
