package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.service.IService;
import com.hq.common.utils.PageUtils;
import com.hq.modules.operate.entity.WarningrecordEntity;

import java.util.Map;

/**
 *
 *
 * @author xxx
 *
 */
public interface WarningrecordService extends IService<WarningrecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
    void dealWarning(WarningrecordEntity warningrecordEntity);
}

