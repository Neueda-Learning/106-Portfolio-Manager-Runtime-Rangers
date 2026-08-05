package com.neueda.portfolio_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI portfolioApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Portfolio Manager API")
                        .version("1.0")
                        .description("REST APIs for Portfolio Manager"));
    }
}