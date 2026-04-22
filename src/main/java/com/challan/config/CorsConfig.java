package com.challan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation
        .CorsRegistry;
import org.springframework.web.servlet.config.annotation
        .WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "https://challan-frontend.vercel.app",
                        "https://challan-frontend-innov-dn.vercel.app",
                        "https://challan-frontend-three.vercel.app/"
                        // paste your exact Vercel URL here
                )
                .allowedMethods(
                        "GET", "POST", "PUT",
                        "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}