package com.postech.scheduling_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHeadersConfig {
    @Bean
    feign.RequestInterceptor originHeader() {
        return tmpl -> tmpl.header("Origin", "http://tc3-scheduling-service");
    }
}