package com.example.image_label.controller;

import com.example.image_label.pojo.Image;
import com.example.image_label.service.ImageService;
import com.example.image_label.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("${image.save.path:D:/img_label/}")
    private String basePath;

    private final ImageService imageService;

    // 使用构造函数注入
    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // 图像上传接口
    @PostMapping("/upload")
    public Result<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Integer userId
    ) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File saveFile = new File(basePath + fileName);

        // 保存文件到本地
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        file.transferTo(saveFile);

        // 保存到数据库的路径改为仅文件名
        Image image = new Image();
        image.setUserId(userId);  // 对应实体类的userId字段
        image.setStoragePath(fileName);  // 存储路径改为仅文件名（对应实体类的storagePath字段）
        image.setFilename(file.getOriginalFilename());  // 原始文件名（对应实体类的filename字段）
        image.setFileSize((int) file.getSize());  // 文件大小（从MultipartFile获取字节数并转换为Integer）
        image.setUploadTime(LocalDateTime.now());  // 上传时间（对应实体类的uploadTime字段）
        image.setAnnotated(false);  // 上传时默认未标注（根据业务需求可选，若无需默认值可省略）
        imageService.save(image);

        return Result.success("上传成功");
    }

    // 获取图像列表（含标注状态）
    @GetMapping("/list")
    public Result<List<Image>> listWithAnnotatedStatus() {
        List<Image> imageList = imageService.listWithAnnotatedStatus();
        return Result.success(imageList);
    }

    // 获取单张图片信息
    @GetMapping("/{imageId}")
    public Result<Image> getImage(@PathVariable Integer imageId) {
        Image image = imageService.getById(imageId);
        if (image == null) {
            return Result.error("图片不存在");
        }
        System.out.println("返回的图片信息: " + image); // 添加调试信息
        return Result.success(image);
    }
}