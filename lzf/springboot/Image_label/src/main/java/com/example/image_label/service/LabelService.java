package com.example.image_label.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.image_label.dao.LabelDao;
import com.example.image_label.pojo.Annotation;
import com.example.image_label.pojo.Image;
import com.example.image_label.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    // 查询用户标签
    public List<Label> getLabelsByUserId(Integer userId) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("created_by", userId); // 直接使用数据库字段名
        return labelDao.selectList(queryWrapper);
    }

    public String generateLabelFile(Image image, List<Annotation> annotations, String format) {
        // 实现生成标签文件的逻辑
        return null;
    }

    public File zipFiles(List<File> files) throws IOException {
        File zipFile = new File("labels.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                if (file.exists()) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);
                    java.io.FileInputStream fis = new java.io.FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                }
            }
        }
        return zipFile;
    }

    // 新增标签
    public Label addLabel(Label label) {
        label.setCreatedAt(LocalDateTime.now());
        // 实际项目中 createdBy 应从用户会话获取（如 SecurityContext）
        label.setCreatedBy(4); // 示例值，需替换为实际逻辑
        labelDao.insert(label); // MyBatis-Plus 的 insert 会自动填充主键（需配置 @TableId）
        return label; // 返回插入后的对象（包含数据库生成的 ID）
    }

    // 更新标签
    public void updateLabel(Label label) {
        labelDao.updateById(label);
    }

    // 删除标签（带关联检查）
    public void deleteLabel(Integer labelId) {
        // 检查是否有关联标注
        long annotationCount = labelDao.selectAnnotationCountByLabelId(labelId);
        if (annotationCount > 0) {
            throw new RuntimeException("该标签已关联标注，无法删除");
        }
        labelDao.deleteById(labelId);
    }
    public Label getLabelByNameAndUser(String labelName, Integer userId) {
        return labelDao.selectOne(
                new QueryWrapper<Label>()
                        .eq("label_name", labelName)
                        .eq("created_by", userId)
        );
    }

}