package com.example.livraison.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Système de Livraison")
                        .description("API REST pour la gestion d'un système complet de livraison")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe de Développement")
                                .email("support@livraison-system.com"))
                        .license(new License()
                                .name("Propriétaire")
                                .url("https://livraison-system.com/license")))
                .servers(List.of(
                        new Server().url("http://localhost:8000").description("Serveur local"),
                        new Server().url("http://127.0.0.1:8000").description("Serveur local (IP)")
                ));
    }
}
