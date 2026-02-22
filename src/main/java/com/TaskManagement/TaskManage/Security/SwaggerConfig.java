package com.TaskManagement.TaskManage.Security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                //  API METADATA (this answers your question)
                .info(new Info()
                        .title("Task Manager API")
                        .version("1.0.0")
                        .description("REST APIs for Task Manager Application with JWT-based authentication"))

                //  SECURITY CONFIG
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"));
    }
}