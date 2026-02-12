package com.sungil.springboot.databrainbackend.api.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 메시지 파일의 기본 이름을 지정 (messages.properties, messages_ko.properties 등)
        messageSource.setBasenames("messages");
        // [핵심] 인코딩을 UTF-8로 강제 고정
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}