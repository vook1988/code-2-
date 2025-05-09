package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.InspectionTask;
import com.baomidou.mybatisplus.service.IService;

public interface IInspectionTaskService extends IService<InspectionTask> {

    /**
     * 分页查询巡查任务
     *
     * @param page           分页对象
     * @param inspectionTask 查询条件
     * @return 分页查询结果
     */
    Page<InspectionTask> queryPageList(Page<InspectionTask> page, InspectionTask inspectionTask);
}