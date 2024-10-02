package br.chat.ChatOnline.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration annotation indicates that this class provides Spring configuration
@Configuration
public class SpringDocConfiguration {

    // Method to define a custom OpenAPI configuration
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key", // Defining a security scheme for API documentation
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP) // Specifies the type of the security scheme
                                        .scheme("bearer") // Indicates the authentication scheme used
                                        .bearerFormat("JWT"))); // Specifies the format of the bearer token
    }
}
