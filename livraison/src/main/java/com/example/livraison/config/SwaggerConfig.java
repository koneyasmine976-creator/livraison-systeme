package com.example.livraison.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Système de Livraison")
                        .version("1.0.0")
                        .description("""
                                ## API REST complète pour la gestion d'un système de livraison
                                
                                Cette API permet de gérer :
                                - 🏪 **Commerçants** : Inscription, gestion des boutiques et des produits
                                - 👥 **Clients** : Inscription, gestion du profil et des adresses
                                - 🚚 **Livreurs** : Inscription, gestion des statuts et des livraisons
                                - 📦 **Commandes** : Création, suivi et gestion des commandes
                                - 🛵 **Demandes de livraison** : Création, assignation et suivi des livraisons
                                - 💬 **Support** : Système de messagerie et support client
                                - 📊 **Statistiques** : Tableaux de bord et rapports
                                
                                ### Authentification
                                L'API utilise un système d'authentification basé sur les sessions.
                                Pour utiliser les endpoints protégés, vous devez d'abord vous connecter via `/api/auth/connexion`.
                                
                                ### Rôles disponibles
                                - `CLIENT` : Accès aux fonctionnalités client
                                - `COMMERCANT` : Accès aux fonctionnalités commerçant
                                - `LIVREUR` : Accès aux fonctionnalités livreur
                                
                                ### Workflow typique
                                1. Inscription (commerçant, client ou livreur)
                                2. Connexion pour obtenir une session
                                3. Utilisation des endpoints selon votre rôle
                                4. Déconnexion à la fin de la session
                                """)
                        .contact(new Contact()
                                .name("Équipe de développement")
                                .email("support@livraison-systeme.com")
                                .url("https://livraison-systeme.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement"),
                        new Server()
                                .url("http://127.0.0.1:8080")
                                .description("Serveur local"),
                        new Server()
                                .url("https://api.livraison-systeme.com")
                                .description("Serveur de production")
                ))
                .components(new Components()
                        .addSecuritySchemes("session", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")
                                .description("Authentification basée sur les sessions Spring")))
                .addSecurityItem(new SecurityRequirement().addList("session"));
    }
}
