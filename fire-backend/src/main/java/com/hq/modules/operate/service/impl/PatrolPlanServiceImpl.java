package com.hq.modules.operate.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.InspectionTask;
import com.hq.modules.operate.entity.PatrolPlan;
import com.hq.modules.operate.dao.PatrolPlanMapper;
import com.hq.modules.operate.service.IPatrolPlanService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * @Description: 巡查计划
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Service
public class PatrolPlanServiceImpl extends ServiceImpl<PatrolPlanMapper, PatrolPlan> implements IPatrolPlanService {
    @Override
    public Page<PatrolPlan> queryPageList(Page<PatrolPlan> page, PatrolPlan patrolPlan) {
        // 进行空值判断，如果传入的 inspectionTask 为 null，则创建一个新的 InspectionTask 对象
        if (patrolPlan == null) {
            patrolPlan = new PatrolPlan();
        }

        // 创建 EntityWrapper 对象，用于构建 SQL 查询条件
        EntityWrapper<PatrolPlan> wrapper = new EntityWrapper<>();

        // 根据 inspectionTask 对象的 name 属性构建模糊查询条件
        // 如果 name 不为 null 且不为空字符串，则添加模糊查询条件
        if (patrolPlan.getName() != null && !patrolPlan.getName().isEmpty()) {
            wrapper.like("name", patrolPlan.getName());
        }

        // 根据 inspectionTask 对象的 status 属性构建相等查询条件
        // 如果 status 不为 null，则添加相等查询条件
        if (patrolPlan.getStatus() != null) {
            wrapper.eq("task_status", patrolPlan.getStatus());
        }

        // 调用父类的 selectPage 方法进行分页查询
        // page 参数指定分页信息，wrapper 参数指定查询条件
        return this.selectPage(page, wrapper);
    }

}