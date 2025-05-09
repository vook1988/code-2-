package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.hq.modules.operate.entity.Robot;

public interface RobotService extends IService<Robot> {
    Page<Robot> queryRobotListWithType(Page<Robot> page, Robot robot);
}