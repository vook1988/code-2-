package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.service.IService;
import com.hq.common.utils.PageUtils;
import com.hq.modules.operate.entity.AdandonrecordEntity;

import java.util.Map;

/**
 *
 *
 * @author xxx
 *
 */
public interface AdandonrecordService extends IService<AdandonrecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

