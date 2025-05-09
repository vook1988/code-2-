package com.hq.modules.operate.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hq.modules.operate.entity.LocationEntity;
import com.hq.modules.operate.entity.LocationStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author xxx
 *
 */
@Mapper
@Repository
public interface LocationDao extends BaseMapper<LocationEntity> {

    void updateRemarkByid(@Param("locationid") String locationid, @Param("remark") String remark);

    void updateStatusByid(@Param("locationid") String locationid, @Param("status") String status);

    /**
     *
     * 根据市(citycode)查询所有区*/
    List<String> selectDistrictBycityCode(@Param("citycode") String citycode);

    /**
     *根据市(citycode)查询所有区
     * */
    List<LocationEntity> selectBydistrict(@Param("district") String citycode);

    /**
     *获取按区分类统计小区数量
     * */
    List<LocationStatistics> getLocationStatistics();

	List<LocationEntity> selectCityList(@Param("province") String province);

	List<LocationEntity> provinceList();
}
