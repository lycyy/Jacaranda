package com.example.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public Authenticatiolnterceptor authenticatiolnterceptor() {
        return new Authenticatiolnterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticatiolnterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/email")
                .excludePathPatterns("/code")
                .excludePathPatterns("/info")
                .excludePathPatterns("/login")
                .excludePathPatterns("/webhook");
        }
    }

