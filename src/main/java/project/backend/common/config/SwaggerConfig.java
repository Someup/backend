package project.backend.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

  private static final String BEARER_TOKEN_PREFIX = "Bearer";

  @Bean
  public OpenAPI openAPI() {
    String securityJwtName = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);
    Components components = new Components()
        .addSecuritySchemes(securityJwtName, new SecurityScheme()
            .name(securityJwtName)
            .type(SecurityScheme.Type.HTTP)
            .scheme(BEARER_TOKEN_PREFIX)
            .bearerFormat(securityJwtName));

    Server local = new Server();
    local.setUrl("http://localhost:8080/");

    Server dev = new Server();
    dev.setUrl("https://api.someup.site/");

    return new OpenAPI()
        .info(apiInfo())
        .addSecurityItem(securityRequirement)
        .components(components)
        .servers(List.of(local, dev));
  }

  private Info apiInfo() {
    return new Info()
        .title("Someup API Page") // API의 제목
        .description("스위프 6기 5팀 API") // API에 대한 설명
        .version("1.0.0"); // API의 버전
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}