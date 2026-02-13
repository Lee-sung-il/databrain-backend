package com.sungil.springboot.databrainbackend.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme; // [중요] models 패키지 확인!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. 보안 방식(JWT) 정의 - 모델 클래스 사용
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 2. 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        // 3. 전체 설정 조립
        return new OpenAPI()
                .info(new Info()
                        .title("DataBrain API 명세서")
                        .description("AI 기반 다국어 지식 마켓플레이스")
                        .version("v1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", bearerAuth))
                .addSecurityItem(securityRequirement);
    }
}