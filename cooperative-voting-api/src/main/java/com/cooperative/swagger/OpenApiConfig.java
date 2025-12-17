package com.cooperative.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${project.version}")
    private String version;

    @Value("${project.name}")
    private String name;

    @Value("${project.description}")
    private String description;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(name)
                        .version(version)
                        .description(description));
    }
}
