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
                .addPathPatterns("/testRefreshToken")
                ;

        registry.addInterceptor(accesstokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/email")
                .excludePathPatterns("/code")
                .excludePathPatterns("/login")
                .excludePathPatterns("/webhook")
                .excludePathPatterns("/verify_pswdcode")
                .excludePathPatterns("/sendCode")
                .excludePathPatterns("/setPswd")
                .excludePathPatterns("/forgotPswd")
                .excludePathPatterns("/getAccesstoken")
                .excludePathPatterns("/checkusername")
                .excludePathPatterns("/Get_Promotion")
                .excludePathPatterns("/select_allGoods")
                .excludePathPatterns("/company/setPswd")
                .excludePathPatterns("/company/verify_pswdcode")
                .excludePathPatterns("/company/login")
                .excludePathPatterns("/company/code")
                .excludePathPatterns("/company/Publish_Promotion")
                .excludePathPatterns("/testRefreshToken")
                .excludePathPatterns("/company/register");
        }
    }

