package com.stylecart.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI styleCartOpenAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("StyleCart API")

                        .description("""
                                Production-inspired E-Commerce Backend
                                Built using Spring Boot 3
                                """)

                        .version("v1.0")

                        .contact(new Contact()
                                .name("Harsh Saroha")
                                .email("harshsaroha432@gmail.com"))

                        .license(new License()
                                .name("Apache 2.0")))

                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation"));
    }
}