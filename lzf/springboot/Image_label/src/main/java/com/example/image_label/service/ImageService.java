package com.example.image_label.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.image_label.dao.AnnotationDao;
import com.example.image_label.dao.ImageDao;
import com.example.image_label.pojo.Annotation;
import com.example.image_label.pojo.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImageService extends ServiceImpl<ImageDao, Image> {

    @Autowired
    private AnnotationDao annotationDao;

    // 查询图像列表并标记是否已标注
    public List<Image> listWithAnnotatedStatus() {
        List<Image> imageList = list(); // 获取所有图像

        // 查询所有已标注的imageId
        LambdaQueryWrapper<Annotation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Annotation::getImageId);
        Set<Integer> annotatedImageIds = annotationDao.selectList(queryWrapper)
                .stream()
                .map(Annotation::getImageId)
                .collect(Collectors.toSet());

//        // 标记标注状态
//        imageList.forEach(image -> {
//            image.setAnnotated(annotatedImageIds.contains(image.getImageId()));
//            // 处理存储路径，去掉本地路径前缀
//            String storagePath = image.getStoragePath();
//            String relativePath = storagePath.replace("D:/img_label/", "");
//            image.setStoragePath(relativePath);
//        });
        return imageList;
    }
}