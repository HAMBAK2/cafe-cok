package com.sideproject.cafe_cok.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@OpenAPIDefinition(
        info = @Info(title = "카페콕 API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(name = " \uD83D\uDCE7 dudghks5722@gmail.com", email = "dudghks5722@gmail.com")),
        servers = {
                @Server(url = "/", description = "Default Server url")
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {

        String jwtSchemeName = "jwtAccessToken";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
