package com.shop.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    private String contextPath = "";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("網上購物平台 API")
                        .description("網上購物平台的RESTful API文檔")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("管理員")
                                .email("admin@example.com")
                                .url("https://github.com/example/shop-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080" + contextPath).description("本地開發環境"),
                        new Server().url("https://api.example.com" + contextPath).description("生產環境")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("使用JWT進行身份驗證，格式: Bearer {token}")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
} 