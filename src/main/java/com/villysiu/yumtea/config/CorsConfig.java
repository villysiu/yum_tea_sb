package com.villysiu.yumtea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Customize this as per your need
        registry.addMapping("/**")  // Apply CORS to all paths
                .allowedOrigins("http://127.0.0.1:8001") // React's local dev URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow any header
                .allowCredentials(true) // Allow credentials (cookies, etc.)
                .maxAge(3600); // Cache pre-flight response for 1 hour
    }
}
