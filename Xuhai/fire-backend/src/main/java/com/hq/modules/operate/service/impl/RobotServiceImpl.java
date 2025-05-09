package com.hq.modules.operate.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hq.modules.operate.dao.RobotMapper;
import com.hq.modules.operate.entity.Robot;
import com.hq.modules.operate.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotServiceImpl extends ServiceImpl<RobotMapper, Robot> implements RobotService {
    @Autowired
    private RobotMapper robotMapper;

    @Override
    public Page<Robot> queryRobotListWithType(Page<Robot> page, Robot robot) {
        page.setRecords(robotMapper.selectRobotListWithType(page, robot));
        return page;
    }
}