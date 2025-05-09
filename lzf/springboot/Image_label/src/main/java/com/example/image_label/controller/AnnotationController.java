package com.example.image_label.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.image_label.pojo.Annotation;
import com.example.image_label.pojo.Image;
import com.example.image_label.service.AnnotationService;
import com.example.image_label.service.ImageService;
import com.example.image_label.service.LabelService;
import com.example.image_label.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 图像标注表 前端控制器
 * </p>
 *
 * @author lzf
 */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/annotation")
public class AnnotationController {
    @Autowired
    private AnnotationService annotationService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LabelService labelService;

    @GetMapping("/list")
    public Result<List<Annotation>> getAnnotationsByImageIdAndLabelId(
            @RequestParam Integer imageId,
            @RequestParam(required = false) Integer labelId
    ) {
        try {
            // 检查参数有效性
            if (imageId == null) {
                return Result.error("图片ID不能为空");
            }

            List<Annotation> annotations;
            if (labelId != null) {
                annotations = annotationService.getByImageIdAndLabelId(imageId, labelId);
            } else {
                annotations = annotationService.getByImageId(imageId);
            }

            // 如果没有数据，返回空列表而不是null
            if (annotations == null) {
                annotations = Collections.emptyList();
            }

            return Result.success(annotations);
        } catch (Exception e) {
            // 添加更详细的日志记录

            return Result.error("查询标注数据失败");
        }
    }
    // 原有的接口代码保持不变
    @PostMapping("/save")
    public Result<Boolean> saveAnnotation(@RequestBody Annotation annotation) {
        boolean success = annotationService.save(annotation);
        return Result.success(success);
    }
    @PostMapping("/generate-label")
    public Result<String> generateLabelFile(
            @RequestParam Integer imageId,
            @RequestParam String format // "VOC"或"COCO"
    ) {
        // 查询该图像的所有标注
        List<Annotation> annotations = annotationService.getByImageId(imageId);
        Image image = imageService.getById(imageId);

        // 生成标签文件
        String filePath = labelService.generateLabelFile(image, annotations, format);

        // 更新annotation表的label_file_path
        annotations.forEach(anno -> {
            anno.setLabelFilePath(filePath);
            annotationService.updateById(anno);
        });

        return Result.success("标签文件生成成功：" + filePath);
    }
    @GetMapping("/getImageIdsByLabelId")
    public Result<List<Integer>> getImageIdsByLabelId(@RequestParam Integer labelId) {
        try {
            // 查询该标签关联的所有imageId
            LambdaQueryWrapper<Annotation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Annotation::getImageId)
                    .eq(Annotation::getLabelId, labelId)
                    .groupBy(Annotation::getImageId);

            List<Integer> imageIds = annotationService.listObjs(queryWrapper, Object::toString)
                    .stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            return Result.success(imageIds);
        } catch (Exception e) {
            return Result.error("查询标签关联的图片ID失败");
        }
    }
}