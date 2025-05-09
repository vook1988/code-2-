package com.example.image_label.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 图像信息表
 * </p>
 *
 * @author lzf
 * @since 2025-05-07
 */
@TableName("t_image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图像ID（主键）
     */
    @TableId(value = "image_id", type = IdType.AUTO)
    private Integer imageId;

    /**
     * 上传用户ID（外键）
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 图像存储路径（如：/images/2024/05/abc.jpg）
     */
    @TableField("storage_path")
    private String storagePath;

    /**
     * 原始文件名（如：cat.jpg）
     */
    private String filename;

    /**
     * 文件大小（单位：字节）
     */
    @TableField("file_size")
    private Integer fileSize;

    /**
     * 上传时间
     */
    @TableField("upload_time")
    private LocalDateTime uploadTime;

    private Boolean annotated;


    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Boolean getAnnotated() {
        return annotated;
    }

    public void setAnnotated(Boolean annotated) {
        this.annotated = annotated;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", userId=" + userId +
                ", storagePath=" + storagePath +
                ", filename=" + filename +
                ", fileSize=" + fileSize +
                ", uploadTime=" + uploadTime +
                ", annotated=" + annotated +
                "}";
    }
}