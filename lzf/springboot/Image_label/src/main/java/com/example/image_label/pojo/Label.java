package com.example.image_label.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标签信息表
 * </p>
 *
 * @author lzf
 *
 */
@TableName("t_label")
public class Label implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID（主键）
     */
    @TableId(value = "label_id", type = IdType.AUTO)
    private Integer labelId;

    /**
     * 标签名称（如“猫”、“车辆”）
     */
    @TableField("label_name")
    private String labelName;

    /**
     * 创建用户ID（外键，关联用户表）
     */
    @TableField("created_by")
    private Integer createdBy;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /**
     * 标签颜色（用于前端展示）
     */
    @TableField("label_color")
    private String labelColor;

    // 新增getter和setter
    public String getLabelColor() {
        return labelColor;
    }
    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }


    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
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

    @Override
    public String toString() {
        return "Label{" +
                "labelId=" + labelId +
                ", labelName=" + labelName +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                "}";
    }
}