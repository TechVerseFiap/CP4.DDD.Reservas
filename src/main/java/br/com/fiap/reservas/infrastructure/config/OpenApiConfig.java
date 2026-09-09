package br.com.fiap.reservas.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Reservas de Equipamentos")
                        .version("1.0.0")
                        .description("API REST para professores, salas, equipamentos e reservas. "
                                + "Erros de negocio sao retornados como RFC 7807 ProblemDetail.")
                        .contact(new Contact().name("Equipe de Desenvolvimento").email("dev@fiap.com.br"))
                        .license(new License().name("Uso academico")));
    }
}
