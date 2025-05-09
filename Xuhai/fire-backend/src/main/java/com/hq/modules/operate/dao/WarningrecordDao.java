package com.hq.modules.operate.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hq.modules.operate.entity.WarningrecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author xxx
 *
 */
@Mapper
public interface WarningrecordDao extends BaseMapper<WarningrecordEntity> {

    void dealWarning(@Param("warningrecord")WarningrecordEntity warningrecordEntity);
}
