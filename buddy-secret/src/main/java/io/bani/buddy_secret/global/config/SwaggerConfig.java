package io.bani.buddy_secret.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Buddy Secret API")
                .description("회원가입 및 암호화 테스트 API")
                .version("v1.0");

        Server server = new Server();

        server.setUrl("http://localhost:8080");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
