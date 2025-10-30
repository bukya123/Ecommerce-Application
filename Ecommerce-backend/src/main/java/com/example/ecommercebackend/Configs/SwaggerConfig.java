package com.example.ecommercebackend.Configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                .bearerFormat("JWT")
                .description("JWT Bearer Token");
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Ecommerce API")
                        .version("1.0")
                        .description("This is a Spring Boot Project for Ecommerce")
                        .license(new License().name("Our License Link").url("http://jaggu.com"))
                        .contact(new Contact()
                                .name("Bukya jagadish")
                                .email("jagadishbukya@.com")
                                .url("https://github.com/bukya123")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("http://jaggu.com"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",securityScheme))
                .addSecurityItem(securityRequirement);

    }
}
