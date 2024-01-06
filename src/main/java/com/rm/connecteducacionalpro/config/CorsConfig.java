package com.rm.connecteducacionalpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig  implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("/chat-websocket")
                .allowedMethods("*")
                .allowedHeaders("*");

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                CorsRegistration cors = registry.addMapping("/**");
                cors.allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS");
                cors.allowedOrigins("*");
                cors.allowedHeaders("*");
            }
        };
    }



}