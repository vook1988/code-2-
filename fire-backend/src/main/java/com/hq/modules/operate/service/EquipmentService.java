package com.hq.modules.operate.service;

import com.baomidou.mybatisplus.service.IService;
import com.hq.common.utils.PageUtils;
import com.hq.modules.operate.entity.EquipmentEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author xxx
 *
 */
public interface EquipmentService extends IService<EquipmentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateAllByid( EquipmentEntity equipmentEntity,String id);

    List<String> selectAllType();

    List<String> selectAllCommunity();

    void updateStatusByid( int equipmentStatus,String id);

	EquipmentEntity getByLoactionId(String locationId);
}

