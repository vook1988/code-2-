package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.InspectionTask;
import com.hq.modules.operate.entity.ProblemLogging;
import com.baomidou.mybatisplus.service.IService;

/**
 * @Description: 问题记录
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
public interface IProblemLoggingService extends IService<ProblemLogging> {

    Page<ProblemLogging> queryPageList(Page<ProblemLogging> page, ProblemLogging problemLogging);

}
