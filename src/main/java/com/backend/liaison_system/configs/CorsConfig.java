package com.backend.liaison_system.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${deployed-backend-url}")
    private String DEPLOYED_FRONT_END_URL;
    @Value("${local-backend-url}")
    private String LOCAL_FRONT_END_URL;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("api/v1/**")
                .allowedOrigins(DEPLOYED_FRONT_END_URL, LOCAL_FRONT_END_URL)
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "UPDATE", "OPTIONS");
    }
}
