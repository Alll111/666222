package com.config;

import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String uploadPath = new File(System.getProperty("user.dir"), "upload").getAbsolutePath() + File.separator;
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:" + uploadPath, "classpath:/static/upload/");
	}
}
