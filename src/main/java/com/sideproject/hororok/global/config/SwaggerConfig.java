package com.sideproject.hororok.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Default Server url")
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {

        Info info = new Info()
                .title("Hororok API 명세서")
                .description("[사용 Tips]\n" +
                        "- API는 Header에 AccessToken이 필요한 경우와 필요 없는 경우로 나누어져 있습니다.\n" +
                        "- 현재 페이지 우측 상단의 Seelct a definition에서 해당 항목을 선택하여 사용해주세요\n\n" +
                        "- AccessToken이 필요한 API테스트 시에는 해당글 우측하단의 Authorize를 클릭해서 AccessToken 값을 입력하여 사용해주세요.");

        String jwtSchemeName = "jwtAccessToken";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }


    @Bean
    public GroupedOpenApi requiredAccessTokenGroup() {
        String[] pathsToMatch = {
                "/api/review/create",
                "/api/favorite/**"
        };


        return GroupedOpenApi.builder()
                .group("AccessToken이 필요한 API")
                .pathsToMatch(pathsToMatch)
                .build();
    }

    @Bean
    public GroupedOpenApi notRequiredAccessTokenGroup() {
        String[] pathsToMatch = {
            "/api/**"
        };

        String[] pathsToExclude = {
                "/api/review/create",
                "/api/favorite/**"
        };


        return GroupedOpenApi.builder()
                .group("AccessToken이 필요없는 API")
                .pathsToMatch(pathsToMatch)
                .pathsToExclude(pathsToExclude)
                .build();
    }
}
