# 📚 Guide Documentation Swagger/OpenAPI

## 🌐 Accès à la Documentation

Une fois votre application Spring Boot démarrée, la documentation Swagger est accessible via :

### Interface Swagger UI
```
http://localhost:8080/swagger-ui.html
```
ou
```
http://127.0.0.1:8080/swagger-ui.html
```

### API Documentation JSON
```
http://localhost:8080/api-docs
```

### API Documentation YAML
```
http://localhost:8080/api-docs.yaml
```

## 🎯 Fonctionnalités de Swagger UI

### 1. Navigation par Tags
La documentation est organisée par modules :
- **Authentification** : Connexion, déconnexion, gestion des sessions
- **Commerçants** : Inscription et gestion des commerçants
- **Clients** : Inscription et gestion des clients
- **Livreurs** : Inscription et gestion des livreurs
- **Demandes de Livraison** : Création, assignation et suivi des livraisons
- **Commandes** : Gestion des commandes
- **Produits** : Catalogue produits
- **Support** : Messagerie support

### 2. Tester les API directement

#### Étape 1 : Inscription d'un utilisateur
1. Développez la section **Commerçants**, **Clients** ou **Livreurs**
2. Cliquez sur `POST /api/commercants/inscription` (ou équivalent)
3. Cliquez sur "Try it out"
4. Remplissez le JSON avec vos données :
```json
{
  "idCommercant": "COM001",
  "nom": "Martin",
  "prenom": "Pierre",
  "telephone": "+33123456789",
  "email": "pierre.martin@commerce.com",
  "motDePasse": "motdepasse123",
  "nomBoutique": "Boutique Martin",
  "adresseBoutique": "123 Rue du Commerce, 75001 Paris"
}
```
5. Cliquez sur "Execute"
6. Consultez la réponse dans la section "Server response"

#### Étape 2 : Connexion
1. Allez dans **Authentification**
2. Sélectionnez `POST /api/auth/connexion`
3. Cliquez sur "Try it out"
4. Remplissez :
```json
{
  "email": "pierre.martin@commerce.com",
  "motDePasse": "motdepasse123",
  "role": "COMMERCANT"
}
```
5. Exécutez - Une session est créée automatiquement

#### Étape 3 : Utiliser les endpoints protégés
Une fois connecté, vous pouvez utiliser tous les endpoints qui nécessitent une session active. Swagger conserve automatiquement les cookies de session.

### 3. Formats de Réponse

Toutes les réponses suivent le format standard `ApiResponse` :

```json
{
  "success": true,
  "message": "Message descriptif",
  "data": {
    // Données de la réponse
  },
  "timestamp": "2024-10-16T20:00:00"
}
```

## 📋 Exemples d'Utilisation Complète

### Workflow Commerçant

```bash
# 1. Inscription commerçant
POST /api/commercants/inscription

# 2. Connexion
POST /api/auth/connexion

# 3. Créer une demande de livraison
POST /api/demandes-livraison/creer

# 4. Voir les livreurs disponibles
GET /api/livreurs/disponibles

# 5. Assigner un livreur
POST /api/demandes-livraison/assigner

# 6. Consulter ses demandes
GET /api/demandes-livraison/mes-demandes
```

### Workflow Livreur

```bash
# 1. Inscription livreur
POST /api/livreurs/inscription

# 2. Connexion
POST /api/auth/connexion

# 3. Voir ses livraisons assignées
GET /api/demandes-livraison/mes-demandes

# 4. Accepter une livraison
POST /api/demandes-livraison/{id}/accepter

# 5. Mettre à jour le statut
PUT /api/demandes-livraison/statut

# 6. Modifier son statut
PUT /api/livreurs/statut?nouveauStatut=DISPONIBLE
```

## 🔐 Authentification et Sécurité

### Sessions
L'API utilise l'authentification par session (JSESSIONID cookie). Swagger UI gère automatiquement les cookies, donc :
- Connectez-vous une fois via `/api/auth/connexion`
- Toutes les requêtes suivantes utiliseront automatiquement la session
- Pour changer d'utilisateur, déconnectez-vous d'abord via `/api/auth/deconnexion`

### Rôles et Permissions

| Endpoint | CLIENT | COMMERCANT | LIVREUR |
|----------|--------|------------|---------|
| POST /api/demandes-livraison/creer | ❌ | ✅ | ❌ |
| POST /api/demandes-livraison/assigner | ❌ | ✅ | ❌ |
| POST /api/demandes-livraison/{id}/accepter | ❌ | ❌ | ✅ |
| GET /api/demandes-livraison/mes-demandes | ❌ | ✅ | ✅ |
| PUT /api/livreurs/statut | ❌ | ❌ | ✅ |

## 🎨 Personnalisation

### Trier les Opérations
Par défaut, les opérations sont triées par méthode HTTP (GET, POST, PUT, DELETE). Vous pouvez les trier alphabétiquement dans les paramètres de Swagger UI.

### Filtrer les Endpoints
Utilisez la barre de recherche en haut de Swagger UI pour filtrer les endpoints par nom, tag ou description.

### Mode Sombre
Swagger UI supporte le mode sombre selon les préférences de votre système.

## 🛠️ Configuration Avancée

### Export de la Spécification OpenAPI

Pour exporter la spécification complète de l'API :

```bash
# Format JSON
curl http://localhost:8080/api-docs > openapi.json

# Format YAML
curl http://localhost:8080/api-docs.yaml > openapi.yaml
```

### Génération de Clients API

Utilisez les spécifications OpenAPI avec des outils comme :
- **Swagger Codegen** : Génère des clients dans différents langages
- **OpenAPI Generator** : Alternative moderne à Swagger Codegen
- **Postman** : Import direct de la spécification OpenAPI

```bash
# Exemple avec OpenAPI Generator
openapi-generator generate -i http://localhost:8080/api-docs \
  -g typescript-axios \
  -o ./generated-client
```

## 📊 Statuts HTTP

| Code | Signification | Exemple |
|------|---------------|---------|
| 200 | Succès | Opération réussie |
| 400 | Requête invalide | Données JSON malformées |
| 401 | Non authentifié | Session expirée ou invalide |
| 403 | Accès refusé | Rôle insuffisant pour l'opération |
| 404 | Non trouvé | Ressource inexistante |
| 500 | Erreur serveur | Erreur interne de l'application |

## 🚀 Démarrage Rapide

### Test Complet en 5 Minutes

1. **Démarrez l'application**
   ```bash
   mvn spring-boot:run
   ```

2. **Ouvrez Swagger UI**
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Inscrivez un commerçant**
   - Section "Commerçants" → POST /inscription
   - Try it out → Remplir le JSON → Execute

4. **Connectez-vous**
   - Section "Authentification" → POST /connexion
   - Utilisez l'email et mot de passe du commerçant

5. **Créez une demande de livraison**
   - Section "Demandes de Livraison" → POST /creer
   - Remplissez les détails → Execute

6. **Explorez les autres endpoints**
   - Toutes les sections sont maintenant accessibles
   - Testez les différents workflows

## 💡 Conseils et Astuces

### 1. Réutiliser les Réponses
Copiez les IDs des réponses pour les utiliser dans d'autres requêtes :
```json
{
  "data": {
    "id": "COM001"  // ← Copiez cet ID
  }
}
```

### 2. Valider les Données
Swagger affiche les contraintes de validation (required, format, etc.) directement dans l'interface.

### 3. Consulter les Schémas
En bas de chaque endpoint, consultez la section "Schemas" pour voir la structure complète des objets.

### 4. Tester les Erreurs
Testez volontairement des cas d'erreur pour voir comment l'API les gère :
- Email déjà utilisé
- Session expirée
- Données manquantes

## 🔄 Intégration Continue

### Tests Automatisés avec Swagger

```javascript
// Exemple avec Newman (Postman CLI)
const swaggerUrl = 'http://localhost:8080/api-docs';
// Import dans Postman → Export collection → Run avec Newman
```

### Validation de Contrat

```bash
# Avec swagger-cli
swagger-cli validate http://localhost:8080/api-docs
```

## 📞 Support

Pour toute question sur l'utilisation de l'API :
- Consultez la documentation inline dans Swagger UI
- Référez-vous au fichier `API_LIVRAISON_DOCUMENTATION.md`
- Contactez l'équipe de développement

---

**Documentation générée automatiquement par SpringDoc OpenAPI v2.2.0**

**Dernière mise à jour** : Octobre 2024
