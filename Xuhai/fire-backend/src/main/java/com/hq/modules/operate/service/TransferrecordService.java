package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.service.IService;
import com.hq.common.utils.PageUtils;
import com.hq.modules.operate.entity.TransferrecordEntity;

import java.util.Map;

/**
 *
 *
 * @author xxx
 *
 */
public interface TransferrecordService extends IService<TransferrecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

