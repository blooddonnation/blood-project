package com.example.demo.corsconfig;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class corsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  
        config.setAllowedOrigins(List.of("http://localhost:8082", "http://localhost:3000"));  
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));  
        config.setAllowedHeaders(List.of("*"));  
        // If you need wildcard subdomain support, use:
        // config.setAllowedOriginPatterns(List.of("http://*.localhost:3000"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return (CorsConfigurationSource) source;
    }
}
