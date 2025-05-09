package com.hq.modules.operate.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.InspectionTask;
import com.hq.modules.operate.entity.ProblemLogging;
import com.hq.modules.operate.dao.ProblemLoggingMapper;
import com.hq.modules.operate.service.IProblemLoggingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * @Description: 问题记录
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Service("ProblemLoggingService")
public class ProblemLoggingServiceImpl extends ServiceImpl<ProblemLoggingMapper, ProblemLogging> implements IProblemLoggingService {

    @Override
    public Page<ProblemLogging> queryPageList(Page<ProblemLogging> page, ProblemLogging problemLogging) {
        // 进行空值判断，如果传入的 inspectionTask 为 null，则创建一个新的 InspectionTask 对象
        if (problemLogging == null) {
            problemLogging = new ProblemLogging();
        }

        // 创建 EntityWrapper 对象，用于构建 SQL 查询条件
        EntityWrapper<ProblemLogging> wrapper = new EntityWrapper<>();

        // 根据 inspectionTask 对象的 name 属性构建模糊查询条件
        // 如果 name 不为 null 且不为空字符串，则添加模糊查询条件
        if (problemLogging.getName() != null && !problemLogging.getName().isEmpty()) {
            wrapper.like("name", problemLogging.getName());
        }

        // 根据 inspectionTask 对象的 status 属性构建相等查询条件
        // 如果 status 不为 null，则添加相等查询条件
        if (problemLogging.getStatus() != null) {
            wrapper.eq("task_status", problemLogging.getStatus());
        }

        // 调用父类的 selectPage 方法进行分页查询
        // page 参数指定分页信息，wrapper 参数指定查询条件
        return this.selectPage(page, wrapper);
    }
}
