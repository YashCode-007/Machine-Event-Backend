package com.factory.machine_events.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI setConfig() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Machine Event Backend")
                                .description("Backend service for ingesting and analyzing machine events")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Yash Rajvanshi")
                                                .url("https://github.com/YashCode-007")
                                )
                );
    }
}
