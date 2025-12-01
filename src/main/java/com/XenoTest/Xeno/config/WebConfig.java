package com.XenoTest.Xeno.config;

import com.XenoTest.Xeno.tenant.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/dashboard.html",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*", "X-Tenant-ID")
                .allowedMethods("*")
                .allowedOrigins("*");
    }
}
