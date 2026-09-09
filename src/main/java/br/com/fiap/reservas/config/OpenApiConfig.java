package br.com.fiap.reservas.config;

import io.swagger.v3.oas.models.Components;
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
                .components(new Components())
                .info(new Info()
                        .title("API de Reservas de Equipamentos")
                        .version("1.0.0")
                        .description("API REST para cadastro de professores, salas, equipamentos e gerenciamento de reservas. " +
                                "A API aplica validações de entrada e regras de negócio para impedir conflitos de reserva e " +
                                "garantir a disponibilidade dos recursos.")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("dev@fiap.com.br"))
                        .license(new License()
                                .name("Uso acadêmico")));
    }
}
