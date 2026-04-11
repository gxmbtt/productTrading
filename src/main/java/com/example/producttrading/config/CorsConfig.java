package com.example.producttrading.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author guoxin
 * @date 2026年04月04日 13:18
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${files.upload.path}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 匹配所有路径 /login /register  /sysuser/list
                .allowedOriginPatterns("*")  // 允许所有源，你也可以配置特定的源，如"http://localhost:51730"
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*")  // 允许所有头
                .allowCredentials(true)  // 允许发送cookie
                .maxAge(3600);  // 预检请求的缓存时间（秒）
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/**").addResourceLocations("file:"+uploadPath);
    }
}
