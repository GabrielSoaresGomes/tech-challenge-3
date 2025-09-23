package com.postech.test_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHeadersConfig {
    @Bean
    feign.RequestInterceptor originHeader() {
        return tmpl -> tmpl.header("Origin", "http://scheduling-service");
    }
}