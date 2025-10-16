# 📚 Documentation Swagger - Système de Livraison

## 🚀 Accès à Swagger UI

### URLs d'accès
- **Swagger UI** : http://localhost:8000/swagger-ui.html
- **API Docs JSON** : http://localhost:8000/api-docs
- **API Docs YAML** : http://localhost:8000/api-docs.yaml

## 🎯 Fonctionnalités Swagger Configurées

### Interface Professionnelle
✅ **Interface moderne** avec thème professionnel
✅ **Documentation complète** de tous les endpoints
✅ **Try it out** activé pour tester directement
✅ **Groupes d'API** organisés par domaine
✅ **Authentification** intégrée (Session & JWT)
✅ **Exemples de requêtes** pour chaque endpoint
✅ **Codes de réponse** documentés
✅ **Modèles de données** avec descriptions

### Groupes d'API Disponibles

#### 🌐 API Publique
- Authentification
- Inscriptions (Clients, Commerçants, Livreurs)

#### 🏪 API Commerçant
- Gestion des boutiques
- Création de demandes de livraison
- Gestion des produits

#### 🚚 API Livreur
- Gestion du profil livreur
- Acceptation/Refus de livraisons
- Mise à jour des statuts

#### 👤 API Client
- Gestion du profil client
- Commandes
- Adresses de livraison

#### ⚙️ API Administration
- Statistiques
- Support
- Monitoring

## 📋 Configuration dans application.properties

```properties
# Port de l'application
server.port=8000

# Configuration Swagger
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.tryItOutEnabled=true
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.filter=true
springdoc.swagger-ui.docExpansion=none
springdoc.swagger-ui.displayRequestDuration=true
```

## 🔐 Authentification dans Swagger

### Pour tester les endpoints protégés :

1. **Inscription** (si nouveau utilisateur)
   - Utilisez l'endpoint approprié dans le groupe "API Publique"
   - `/api/commercants/inscription`
   - `/api/livreurs/inscription`
   - `/api/clients/inscription`

2. **Connexion**
   - Endpoint : `POST /api/auth/connexion`
   - Body :
   ```json
   {
     "email": "votre.email@example.com",
     "motDePasse": "votre_mot_de_passe",
     "role": "COMMERCANT" // ou "LIVREUR" ou "CLIENT"
   }
   ```

3. **Utilisation de la session**
   - La session est automatiquement gérée par le navigateur
   - Les cookies de session sont envoyés avec chaque requête

## 🎨 Personnalisation de l'Interface

### Tags avec Emojis
- 🔐 Authentification
- 🏪 Commerçants
- 🚚 Livreurs
- 👤 Clients
- 📦 Demandes de Livraison
- 🛒 Commandes
- 📍 Adresses
- 🛍️ Produits
- 💬 Support
- 📊 Statistiques
- 🧾 Reçus

### Informations de Contact
- **Email** : support@livraison-system.com
- **Documentation** : https://docs.livraison-system.com
- **Termes de Service** : https://livraison-system.com/terms

## 📦 Dépendances Maven

```xml
<!-- SpringDoc OpenAPI 3 / Swagger -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

## 🛠️ Utilisation Avancée

### Export de la Documentation

#### Format JSON
```bash
curl http://localhost:8000/api-docs > api-documentation.json
```

#### Format YAML
```bash
curl http://localhost:8000/api-docs.yaml > api-documentation.yaml
```

### Import dans Postman
1. Ouvrez Postman
2. Cliquez sur "Import"
3. Collez l'URL : `http://localhost:8000/api-docs`
4. Postman générera automatiquement une collection

### Génération de Client SDK

Utilisez OpenAPI Generator :
```bash
# JavaScript/TypeScript
openapi-generator-cli generate -i http://localhost:8000/api-docs -g typescript-axios -o ./sdk

# Java
openapi-generator-cli generate -i http://localhost:8000/api-docs -g java -o ./sdk

# Python
openapi-generator-cli generate -i http://localhost:8000/api-docs -g python -o ./sdk
```

## 🔍 Fonctionnalités de Recherche et Filtrage

- **Recherche globale** : Utilisez la barre de recherche pour trouver rapidement un endpoint
- **Filtrage par tags** : Cliquez sur les tags pour filtrer les endpoints
- **Tri** : Les endpoints sont triés par méthode HTTP (GET, POST, PUT, DELETE)

## 📊 Métriques et Performance

- **Durée des requêtes** : Affichage du temps de réponse pour chaque appel
- **Taille des réponses** : Information sur la taille des données retournées
- **Codes de statut** : Visualisation claire des codes de réponse

## 🚨 Troubleshooting

### Swagger UI ne s'affiche pas
1. Vérifiez que l'application est démarrée sur le port 8000
2. Vérifiez l'URL : http://localhost:8000/swagger-ui.html
3. Vérifiez les logs pour des erreurs de configuration

### Erreur 403 Forbidden
- Les endpoints Swagger sont configurés comme publics dans SecurityConfig
- Vérifiez que les patterns sont bien ajoutés dans la configuration

### Pas de documentation pour certains endpoints
- Assurez-vous que les contrôleurs sont annotés avec `@RestController`
- Vérifiez que les méthodes ont les bonnes annotations (`@GetMapping`, `@PostMapping`, etc.)

## 📝 Bonnes Pratiques

1. **Documentez vos DTOs** avec `@Schema`
2. **Ajoutez des exemples** avec `@ExampleObject`
3. **Décrivez les paramètres** avec `@Parameter`
4. **Documentez les réponses** avec `@ApiResponse`
5. **Groupez les endpoints** par fonctionnalité

## 🎯 Exemple d'Annotation Complète

```java
@Operation(
    summary = "Créer une demande de livraison",
    description = "Permet à un commerçant de créer une nouvelle demande de livraison",
    tags = {"📦 Demandes de Livraison"}
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Demande créée avec succès",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = DemandeDelivraison.class)
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Données invalides"
    ),
    @ApiResponse(
        responseCode = "401",
        description = "Non authentifié"
    )
})
@PostMapping("/creer")
public ResponseEntity<ApiResponse<DemandeDelivraison>> creerDemande(
    @RequestBody @Valid CreerDemandeDelivraisonRequest request
) {
    // Implementation
}
```

## 🌟 Points Forts de Notre Configuration

1. **Multi-environnement** : Support dev/staging/prod
2. **Sécurisé** : Authentification intégrée
3. **Professionnel** : Interface moderne et intuitive
4. **Complet** : Documentation exhaustive
5. **Performant** : Optimisé pour la production
6. **Extensible** : Facile à personnaliser

---

📌 **Note** : L'application est configurée pour fonctionner sur le port **8000** comme demandé.
