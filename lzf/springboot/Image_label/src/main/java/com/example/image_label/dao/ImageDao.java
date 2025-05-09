package com.example.image_label.dao;

import com.example.image_label.pojo.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 图像信息表 Mapper 接口
 * </p>
 *
 * @author lzf
 *
 */
@Mapper
public interface ImageDao extends BaseMapper<Image> {

}
