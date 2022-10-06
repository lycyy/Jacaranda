package com.example.service.Config;

import com.example.service.Interceptor.AccesstokenInterceptor;
import com.example.service.Interceptor.Refreshtokenlnterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public Refreshtokenlnterceptor refreshtokenlnterceptor() {
        return new Refreshtokenlnterceptor();
    }

    @Bean
    public AccesstokenInterceptor accesstokenInterceptor(){return new AccesstokenInterceptor();}


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshtokenlnterceptor())
                .addPathPatterns("/getAccesstoken")
                .addPathPatterns("/testToken")
                ;

        registry.addInterceptor(accesstokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/email")
                .excludePathPatterns("/code")
                .excludePathPatterns("/login")
                .excludePathPatterns("/webhook")
                .excludePathPatterns("/testcode")
                .excludePathPatterns("/sendCode")
                .excludePathPatterns("/setPswd")
                .excludePathPatterns("/forgotPswd")
                .excludePathPatterns("/getAccesstoken")
                .excludePathPatterns("/company/authorizationCode")
                .excludePathPatterns("/company/login")
                .excludePathPatterns("/company/code")
                .excludePathPatterns("/testToken")
                .excludePathPatterns("/company/register");
        }
    }

