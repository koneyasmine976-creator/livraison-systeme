# Système de Gestion des Livraisons - APIs Complètes

## Description
Ce projet Spring Boot fournit un système complet de gestion des livraisons avec des APIs pour l'authentification, la gestion des commandes, des produits, des adresses, du support client et des statistiques.

## Fonctionnalités Principales
- ✅ **Authentification** : Inscription, connexion, déconnexion des clients et commerçants
- ✅ **Gestion des Commandes** : Création, suivi, modification des statuts
- ✅ **Gestion des Produits** : Catalogue, stock, catégories
- ✅ **Gestion des Adresses** : Adresses de livraison pour les clients
- ✅ **Support Client** : Messages et assistance
- ✅ **Statistiques** : Tableaux de bord et rapports
- ✅ **Reçus PDF** : Génération automatique de reçus
- ✅ **Sécurité** : Gestion des rôles et permissions

## Prérequis
- Java 21
- Maven 3.6+

## Installation et Démarrage

### 1. Cloner le projet
```bash
git clone <repository-url>
cd livraison
```

### 2. Compiler le projet
```bash
mvn clean compile
```

### 3. Démarrer l'application
```bash
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## Structure du Projet

```
src/main/java/com/example/livraison/
├── entity/                 # Entités JPA
│   ├── Client.java
│   ├── Commercant.java
│   ├── Commande.java
│   ├── Produit.java
│   ├── Adresse.java
│   ├── LigneCommande.java
│   └── MessageSupport.java
├── repository/             # Repositories JPA
│   ├── ClientRepository.java
│   ├── CommercantRepository.java
│   ├── CommandeRepository.java
│   ├── ProduitRepository.java
│   ├── AdresseRepository.java
│   └── MessageSupportRepository.java
├── dto/                    # Data Transfer Objects
│   ├── InscriptionClientRequest.java
│   ├── InscriptionCommercantRequest.java
│   ├── ConnexionRequest.java
│   ├── CreerCommandeRequest.java
│   ├── CommandeResponse.java
│   ├── ProduitRequest.java
│   ├── AdresseRequest.java
│   ├── MessageSupportRequest.java
│   ├── StatistiquesResponse.java
│   ├── ApiResponse.java
│   └── UserInfo.java
├── service/                # Services métier
│   ├── ClientService.java
│   ├── CommercantService.java
│   ├── AuthService.java
│   ├── CommandeService.java
│   ├── ProduitService.java
│   ├── AdresseService.java
│   ├── MessageSupportService.java
│   ├── StatistiquesService.java
│   └── ReceiptService.java
├── controller/             # Contrôleurs REST
│   ├── AuthController.java
│   ├── ClientController.java
│   ├── CommercantController.java
│   ├── CommandeController.java
│   ├── ProduitController.java
│   ├── AdresseController.java
│   ├── MessageSupportController.java
│   ├── StatistiquesController.java
│   └── ReceiptController.java
├── exception/              # Gestion des erreurs
│   ├── GlobalExceptionHandler.java
│   ├── UserAlreadyExistsException.java
│   ├── UserNotFoundException.java
│   ├── InvalidCredentialsException.java
│   ├── AccountBlockedException.java
│   ├── InvalidRoleException.java
│   ├── CommandeNotFoundException.java
│   ├── InsufficientStockException.java
│   ├── UnauthorizedAccessException.java
│   └── ProduitNotFoundException.java
└── config/                 # Configuration
    └── SecurityConfig.java
```

## APIs Disponibles

### 🔐 **Authentification**

#### Inscription Client
**POST** `/api/client/inscription`
- **Rôle requis :** Aucun
- **Fonctionnalité :** Créer un compte client
- **Validation :** Email unique, mot de passe min 6 caractères

#### Inscription Commerçant  
**POST** `/api/commercant/inscription`
- **Rôle requis :** Aucun
- **Fonctionnalité :** Créer un compte commerçant
- **Validation :** Email unique, informations boutique

#### Connexion
**POST** `/api/auth/connexion`
- **Rôle requis :** Aucun
- **Fonctionnalité :** Se connecter avec email/mot de passe
- **Rôles supportés :** CLIENT, COMMERCANT

#### Déconnexion
**POST** `/api/auth/deconnexion`
- **Rôle requis :** Session active
- **Fonctionnalité :** Fermer la session

#### Vérification de Session
**GET** `/api/auth/session`
- **Rôle requis :** Session active
- **Fonctionnalité :** Vérifier l'état de la session

### 🛒 **Gestion des Commandes**

#### Créer une commande
**POST** `/api/commandes/creer`
- **Rôle requis :** CLIENT
- **Fonctionnalité :** Créer une nouvelle commande
- **Préconditions :** Adresse de livraison enregistrée
- **Validation :** Stock disponible, produits valides

#### Consulter mes commandes
**GET** `/api/commandes/mes-commandes`
- **Rôle requis :** CLIENT ou COMMERCANT
- **Fonctionnalité :** Lister les commandes de l'utilisateur

#### Consulter une commande
**GET** `/api/commandes/{id}`
- **Rôle requis :** CLIENT ou COMMERCANT
- **Fonctionnalité :** Détails d'une commande spécifique
- **Sécurité :** Vérification des droits d'accès

#### Modifier le statut d'une commande
**PUT** `/api/commandes/{id}/statut`
- **Rôle requis :** COMMERCANT
- **Fonctionnalité :** Changer le statut (EN_ATTENTE, EN_COURS, PRETE, EN_LIVRAISON, LIVREE, ANNULEE)

### 📦 **Gestion des Produits**

#### Créer un produit
**POST** `/api/produits/creer`
- **Rôle requis :** COMMERCANT
- **Fonctionnalité :** Ajouter un nouveau produit au catalogue
- **Validation :** Prix > 0, nom obligatoire

#### Consulter mes produits
**GET** `/api/produits/mes-produits`
- **Rôle requis :** COMMERCANT
- **Fonctionnalité :** Lister les produits du commerçant

#### Consulter le catalogue
**GET** `/api/produits/catalogue`
- **Rôle requis :** Aucun
- **Fonctionnalité :** Voir tous les produits actifs

#### Supprimer un produit
**DELETE** `/api/produits/{id}`
- **Rôle requis :** COMMERCANT
- **Fonctionnalité :** Désactiver un produit

### 🏠 **Gestion des Adresses**

#### Créer une adresse
**POST** `/api/adresses/creer`
- **Rôle requis :** CLIENT
- **Fonctionnalité :** Ajouter une adresse de livraison
- **Validation :** Adresse complète obligatoire

#### Consulter mes adresses
**GET** `/api/adresses/mes-adresses`
- **Rôle requis :** CLIENT
- **Fonctionnalité :** Lister les adresses du client

#### Supprimer une adresse
**DELETE** `/api/adresses/{id}`
- **Rôle requis :** CLIENT
- **Fonctionnalité :** Supprimer une adresse

### 📞 **Support Client**

#### Envoyer un message
**POST** `/api/support/envoyer`
- **Rôle requis :** CLIENT ou COMMERCANT
- **Fonctionnalité :** Contacter le service client
- **Validation :** Objet et contenu obligatoires

#### Consulter mes messages
**GET** `/api/support/mes-messages`
- **Rôle requis :** CLIENT ou COMMERCANT
- **Fonctionnalité :** Voir l'historique des messages

#### Consulter tous les messages (Admin)
**GET** `/api/support/tous-messages`
- **Rôle requis :** ADMIN
- **Fonctionnalité :** Voir tous les messages du support

#### Répondre à un message
**POST** `/api/support/{id}/repondre`
- **Rôle requis :** ADMIN
- **Fonctionnalité :** Répondre aux demandes de support

### 📊 **Statistiques**

#### Consulter les statistiques générales
**GET** `/api/statistiques/generales`
- **Rôle requis :** ADMIN ou COMMERCANT
- **Fonctionnalité :** Voir les indicateurs globaux
- **Données :** Commandes, clients, chiffre d'affaires, top produits

#### Consulter les statistiques par période
**GET** `/api/statistiques/periode?debut=2024-01-01T00:00:00&fin=2024-12-31T23:59:59`
- **Rôle requis :** ADMIN ou COMMERCANT
- **Fonctionnalité :** Statistiques sur une période donnée

#### Exporter en PDF
**GET** `/api/statistiques/export/pdf`
- **Rôle requis :** ADMIN ou COMMERCANT
- **Fonctionnalité :** Générer un rapport PDF

#### Exporter en Excel
**GET** `/api/statistiques/export/excel`
- **Rôle requis :** ADMIN ou COMMERCANT
- **Fonctionnalité :** Générer un rapport Excel

### 🧾 **Reçus et Documents**

#### Télécharger un reçu
**GET** `/api/receipts/{commandeId}/download`
- **Rôle requis :** CLIENT ou COMMERCANT
- **Fonctionnalité :** Télécharger le reçu PDF d'une commande
- **Format :** PDF avec détails complets

## Test des APIs

### Avec PowerShell (Windows)
```powershell
# Exécuter le script de test
.\test-api.ps1
```

### Avec cURL (Linux/Mac)
```bash
# Test d'inscription client
curl -X POST http://localhost:8080/api/client/inscription \
  -H "Content-Type: application/json" \
  -d '{
    "idClient": "CLI001",
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@email.com",
    "telephone": "+33123456789",
    "motDePasse": "motdepasse123",
    "adresse": "123 Rue de la Paix, Paris, France",
    "paysResidence": "France"
  }'
```

## Base de Données

### Console H2
Accédez à la console H2 pour visualiser les données :
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Tables créées
- `clients` - Informations des clients
- `commercants` - Informations des commerçants  
- `commandes` - Commandes avec statuts et adresses
- `produits` - Catalogue des produits
- `adresses` - Adresses de livraison des clients
- `lignes_commande` - Détails des produits dans les commandes
- `messages_support` - Messages du support client

## Sécurité et Permissions

### Rôles et Accès
- **CLIENT** : Gestion de ses commandes, adresses, support
- **COMMERCANT** : Gestion de ses produits, commandes, statistiques
- **ADMIN** : Accès complet, gestion du support, toutes les statistiques

### Sécurité Implémentée
- Mots de passe chiffrés avec BCrypt
- Gestion des sessions avec cookies sécurisés
- Validation des données d'entrée avec Bean Validation
- Protection CSRF désactivée pour les APIs REST
- CORS configuré pour permettre les requêtes cross-origin
- Vérification des permissions par rôle
- Gestion centralisée des exceptions

## Configuration

### application.properties
```properties
# Base de données H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true

# Server
server.port=8080
```

## Développement

### Ajout de nouvelles fonctionnalités
1. Créer l'entité dans `entity/`
2. Créer le repository dans `repository/`
3. Créer les DTOs dans `dto/`
4. Créer le service dans `service/`
5. Créer le contrôleur dans `controller/`
6. Mettre à jour la configuration de sécurité si nécessaire

### Tests
```bash
# Exécuter tous les tests
mvn test

# Exécuter un test spécifique
mvn test -Dtest=AuthControllerTest
```

## Cas d'Utilisation Implémentés

### ✅ **CU14 : Créer une commande**
- **Acteur :** Client
- **Préconditions :** Client connecté, adresse enregistrée
- **Fonctionnalité :** Création de commandes avec validation du stock

### ✅ **CU7 : Gérer une commande**  
- **Acteur :** Commerçant, Administrateur
- **Préconditions :** Commande créée, acteur connecté
- **Fonctionnalité :** Suivi et modification des statuts

### ✅ **CU16 : Contacter le service client**
- **Acteur :** Client, Commerçant
- **Préconditions :** Acteur connecté
- **Fonctionnalité :** Messages de support avec gestion des réponses

### ✅ **CU17 : Télécharger un reçu de livraison**
- **Acteur :** Client, Commerçant
- **Préconditions :** Livraison terminée, acteur connecté
- **Fonctionnalité :** Génération PDF automatique

### ✅ **CU18 : Consulter les statistiques**
- **Acteur :** Administrateur, Commerçant
- **Préconditions :** Acteur connecté
- **Fonctionnalité :** Tableaux de bord et rapports

## Documentation API
- `API_DOCUMENTATION.md` - Documentation des APIs d'authentification
- `APIS_COMMANDES.md` - Documentation des APIs de commandes
- `GESTION_ERREURS.md` - Gestion des erreurs et messages

## Support
Pour toute question ou problème, consultez les logs de l'application ou la documentation Spring Boot.
