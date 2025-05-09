package com.hq.modules.operate.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hq.modules.operate.entity.InspectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 巡查任务
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Mapper
@Repository
public interface InspectionTaskMapper extends BaseMapper<InspectionTask> {

}