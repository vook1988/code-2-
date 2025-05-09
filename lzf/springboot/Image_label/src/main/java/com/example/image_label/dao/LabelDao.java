package com.example.image_label.dao;

import com.example.image_label.pojo.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 标签信息表 Mapper 接口
 * </p>
 *
 * @author lzf
 *
 */
@Mapper
public interface LabelDao extends BaseMapper<Label> {

    @Select("SELECT COUNT(*) FROM t_annotation WHERE label_id = #{labelId}")
    long selectAnnotationCountByLabelId(Integer labelId);
}
