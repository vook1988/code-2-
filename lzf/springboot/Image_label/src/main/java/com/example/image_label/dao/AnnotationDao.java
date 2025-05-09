package com.example.image_label.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.image_label.pojo.Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnnotationDao extends BaseMapper<Annotation> {

    List<Annotation> selectAnnotationsByLabelId(Integer labelId);
}