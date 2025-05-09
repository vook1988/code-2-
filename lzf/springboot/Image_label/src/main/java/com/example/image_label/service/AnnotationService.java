package com.example.image_label.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.image_label.dao.AnnotationDao;
import com.example.image_label.pojo.Annotation;
import com.example.image_label.pojo.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnotationService extends ServiceImpl<AnnotationDao, Annotation> {

    @Autowired
    private ImageService imageService;

    public List<Annotation> getByImageId(Integer imageId) {
        LambdaQueryWrapper<Annotation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Annotation::getImageId, imageId);
        return this.list(queryWrapper);
    }

    public boolean save(Annotation annotation) {
        return super.save(annotation);
    }

    public String generateLabelFile(Integer imageId, String format) {
        Image image = imageService.getById(imageId);
        List<Annotation> annotations = getByImageId(imageId);

        String filePath = "D:/img_label/" + image.getFilename() + "_" + format + ".xml"; // 或.json
        if ("VOC".equals(format)) {
            // 生成VOC XML文件
            generateVOCXml(image, annotations, filePath);
        } else if ("COCO".equals(format)) {
            // 生成COCO JSON文件
            generateCOCOJson(image, annotations, filePath);
        }
        // 更新annotation表的label_file_path
        annotations.forEach(anno -> {
            anno.setLabelFilePath(filePath);
            updateById(anno);
        });
        return filePath;
    }
    private void generateVOCXml(Image image, List<Annotation> annotations, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // 根元素
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("annotation");
            doc.appendChild(rootElement);

            // 图像文件名
            Element filename = doc.createElement("filename");
            filename.appendChild(doc.createTextNode(image.getFilename()));
            rootElement.appendChild(filename);

            // 标注信息
            for (Annotation annotation : annotations) {
                Element object = doc.createElement("object");
                rootElement.appendChild(object);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode("label_name")); // 这里需要根据实际情况替换
                object.appendChild(name);

                // 其他标注信息...
            }

            // 保存 XML 文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new java.io.File(filePath));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void generateCOCOJson(Image image, List<Annotation> annotations, String filePath) {
        try {
            Map<String, Object> cocoJson = new HashMap<>();

            // 图像信息
            Map<String, Object> imageInfo = new HashMap<>();
            imageInfo.put("file_name", image.getFilename());
            // 其他图像信息...

            cocoJson.put("images", List.of(imageInfo));

            // 标注信息
            Map<String, Object> annotationInfo = new HashMap<>();
            // 其他标注信息...

            cocoJson.put("annotations", List.of(annotationInfo));

            // 保存 JSON 文件
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), cocoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Annotation> getByImageIdAndLabelId(Integer imageId, Integer labelId) {
        // 这里需要根据实际的数据库操作实现
        // 示例：使用 MyBatis-Plus 的 LambdaQueryWrapper
        LambdaQueryWrapper<Annotation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Annotation::getImageId, imageId)
                .eq(Annotation::getLabelId, labelId);
        return baseMapper.selectList(queryWrapper);
    }


}