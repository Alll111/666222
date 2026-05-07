package com.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.interceptor.AuthorizationInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{

    private String getUploadLocation() {
        File uploadDir = new File("src/main/resources/static/upload").getAbsoluteFile();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String uploadLocation = uploadDir.toURI().toString();
        return uploadLocation.endsWith("/") ? uploadLocation : uploadLocation + "/";
    }

    private String getAdminRootLocation() {
        File adminDir = new File("backend/src/main/resources/admin/admin").getAbsoluteFile();
        String adminLocation = adminDir.toURI().toString();
        return adminLocation.endsWith("/") ? adminLocation : adminLocation + "/";
    }

    private String getAdminDistLocation() {
        File adminDistDir = new File("backend/src/main/resources/admin/admin/dist").getAbsoluteFile();
        String adminDistLocation = adminDistDir.toURI().toString();
        return adminDistLocation.endsWith("/") ? adminDistLocation : adminDistLocation + "/";
    }
	
	@Bean
    public AuthorizationInterceptor getAuthorizationInterceptor() {
        return new AuthorizationInterceptor();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthorizationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/static/**",
                        "/upload/**",
                        "/front/**",
                        "/admin/**",
                        "/admin/dist/**",
                        "/admin/login",
                        "/admin/login.html"
                );
        super.addInterceptors(registry);
	}
	
	/**
	 * springboot 2.0配置WebMvcConfigurationSupport之后，会导致默认配置被覆盖，要访问静态资源需要重写addResourceHandlers方法
	 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**")
        .addResourceLocations(getUploadLocation(), "classpath:/static/upload/");

		registry.addResourceHandler("/admin/dist/**")
        .addResourceLocations(getAdminDistLocation());

		registry.addResourceHandler("/admin/**")
        .addResourceLocations(getAdminRootLocation());

		registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/resources/")
        .addResourceLocations("classpath:/static/")
        .addResourceLocations("classpath:/admin/")
        .addResourceLocations("classpath:/front/")
        .addResourceLocations("classpath:/public/");
		super.addResourceHandlers(registry);
    }
}
