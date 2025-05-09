package com.example.image_label.controller;

import com.example.image_label.pojo.Annotation;
import com.example.image_label.pojo.Label;
import com.example.image_label.service.AnnotationService;
import com.example.image_label.service.LabelService;
import com.example.image_label.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;
    @Autowired
    private AnnotationService annotationService;

    // 查询当前用户的标签列表
    @GetMapping("/list")
    public Map<String, Object> getLabelList(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Label> labels = labelService.getLabelsByUserId(userId);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", labels);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadLabelFiles(
            @RequestParam List<Integer> labelIds,
            HttpServletResponse response
    ) throws IOException {
        // 查询标签文件路径
        List<Annotation> annotations = annotationService.listByIds(labelIds);
        List<File> files = annotations.stream()
                .map(anno -> new File(anno.getLabelFilePath()))
                .collect(Collectors.toList());

        // 打包成压缩文件（通过 Service 处理）
        File zipFile = labelService.zipFiles(files);

        // 设置响应头
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=labels.zip");

        return ResponseEntity.ok()
                .contentLength(zipFile.length())
                .body(new FileSystemResource(zipFile));
    }

    // 新增标签（修复：通过 Service 完成数据库操作）
    @PostMapping("/add")
    public Result<Label> addLabel(@RequestBody Label label) {
        label.setCreatedAt(LocalDateTime.now());
        // 调用 Service 的 addLabel 方法（返回插入后的 Label 对象）
        Label savedLabel = labelService.addLabel(label);
        return Result.success(savedLabel);
    }

    // 更新标签
    @PostMapping("/update")
    public Map<String, Object> updateLabel(@RequestBody Label label) {
        Map<String, Object> result = new HashMap<>();
        try {
            labelService.updateLabel(label);
            result.put("code", 200);
            result.put("msg", "标签更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "更新失败：" + e.getMessage());
        }
        return result;
    }

    // 删除标签
    @DeleteMapping("/delete/{labelId}")
    public Map<String, Object> deleteLabel(@PathVariable Integer labelId) {
        Map<String, Object> result = new HashMap<>();
        try {
            labelService.deleteLabel(labelId);
            result.put("code", 200);
            result.put("msg", "标签删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/getByName")
    public Result<Label> getLabelByName(
            @RequestParam String labelName,
            @RequestParam Integer userId
    ) {
        Label label = labelService.getLabelByNameAndUser(labelName, userId);
        return Result.success(label);
    }
}