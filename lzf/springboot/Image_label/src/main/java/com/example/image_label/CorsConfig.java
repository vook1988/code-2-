package com.example.image_label;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author： LZF
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // 或改为 "*" （生产环境不推荐）
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*")
                .allowedMethods("*"); // 允许所有HTTP方法（包括OPTIONS）
    }
}