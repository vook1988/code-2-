package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.InspectionTask;
import com.hq.modules.operate.entity.PatrolPlan;
import com.baomidou.mybatisplus.service.IService;

/**
 * @Description: 巡查计划
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
public interface IPatrolPlanService extends IService<PatrolPlan> {
    Page<PatrolPlan> queryPageList(Page<PatrolPlan> page, PatrolPlan patrolPlan);

}
