package com.example.image_label.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 图像标注表
 * </p>
 *
 * @author lzf
 * @since 2025-04-30
 */
@TableName("t_annotation")
public class Annotation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标注ID（主键）
     */
    @TableId(value = "annotation_id", type = IdType.AUTO)
    private Integer annotationId;

    /**
     * 关联图像ID（外键）
     */
    @TableField("image_id")
    private Integer imageId;

    /**
     * 关联标签ID（外键）
     */
    @TableField("label_id")
    private Integer labelId;

    /**
     * 标注坐标数据（如：矩形[[x1,y1],[x2,y2]]，多边形[[x1,y1],[x2,y2],[x3,y3]]）
     */
    private String coordinates;

    /**
     * 标注类型（rectangle/rect：矩形；polygon：多边形；point：点）
     */
    @TableField("annotation_type")
    private String annotationType;

    /**
     * 创建用户ID（外键）
     */
    @TableField("created_by")
    private Integer createdBy;

    /**
     * 标注创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    private String labelFilePath; // 标签文件路径
    private String format;        // 格式（VOC/COCO）

    // Getter和Setter
    public String getLabelFilePath() {
        return labelFilePath;
    }

    public void setLabelFilePath(String labelFilePath) {
        this.labelFilePath = labelFilePath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(Integer annotationId) {
        this.annotationId = annotationId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @TableField(exist = false) // 非数据库字段
    private String labelName;

    // Getter 和 Setter
    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
    @Override
    public String toString() {
        return "Annotation{" +
                "annotationId=" + annotationId +
                ", imageId=" + imageId +
                ", labelId=" + labelId +
                ", coordinates=" + coordinates +
                ", annotationType=" + annotationType +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}