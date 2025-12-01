package com.XenoTest.Xeno.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 🚫 IMPORTANT:
    // We REMOVE addInterceptors() completely.
    // TenantFilter + JwtFilter handle everything now.
}
