package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.ProblemLogging;
import com.hq.modules.operate.entity.Route;
import com.baomidou.mybatisplus.service.IService;

/**
 * @Description: 路线管理
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
public interface IRouteService extends IService<Route> {
    Page<Route> queryPageList(Page<Route> page, Route route);

}
