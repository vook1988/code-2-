package com.hq.modules.operate.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hq.modules.operate.dao.RobotTypeMapper;
import com.hq.modules.operate.entity.RobotType;
import com.hq.modules.operate.service.RobotTypeService;
import org.springframework.stereotype.Service;

@Service
public class RobotTypeServiceImpl extends ServiceImpl<RobotTypeMapper, RobotType> implements RobotTypeService {
}