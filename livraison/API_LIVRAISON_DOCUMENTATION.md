# Documentation API Système de Livraison

## Vue d'ensemble

Ce document décrit les API du système de livraison pour permettre :
- L'inscription et la gestion des commerçants
- L'inscription et la gestion des livreurs
- La création et gestion des demandes de livraison
- L'assignation des livreurs aux demandes
- Le suivi des livraisons

## Entités Principales

### Commerçant
- **ID** : Identifiant unique du commerçant
- **Informations personnelles** : nom, prénom, email, téléphone
- **Informations boutique** : nom, adresse, logo
- **Authentification** : email/mot de passe avec rôle ROLE_COMMERCANT

### Livreur
- **ID** : Identifiant unique du livreur
- **Informations personnelles** : nom, prénom, email, téléphone
- **Informations véhicule** : type, plaque, numéro de permis
- **Statut** : DISPONIBLE, OCCUPE, HORS_SERVICE
- **Authentification** : email/mot de passe avec rôle ROLE_LIVREUR

### DemandeDelivraison
- **Informations collecte/livraison** : adresses, destinataire
- **Détails colis** : description, poids, valeur déclarée
- **Statuts** : EN_ATTENTE, ASSIGNEE, ACCEPTEE, EN_COLLECTE, COLLECTEE, EN_LIVRAISON, LIVREE, ANNULEE, REFUSEE
- **Priorités** : BASSE, NORMALE, HAUTE, URGENTE

## API Endpoints

### 🏪 Gestion des Commerçants

#### POST `/api/commercants/inscription`
Inscription d'un nouveau commerçant
```json
{
  "idCommercant": "COM001",
  "nom": "Martin",
  "prenom": "Pierre",
  "telephone": "+33123456789",
  "email": "pierre.martin@commerce.com",
  "motDePasse": "motdepasse123",
  "nomBoutique": "Boutique Martin",
  "adresseBoutique": "123 Rue du Commerce, 75001 Paris",
  "logoBoutique": "logo_url_optionnel"
}
```

### 🚚 Gestion des Livreurs

#### POST `/api/livreurs/inscription`
Inscription d'un nouveau livreur
```json
{
  "idLivreur": "LIV001",
  "nom": "Dupont",
  "prenom": "Jean",
  "telephone": "+33123456789",
  "email": "jean.dupont@livreur.com",
  "motDePasse": "motdepasse123",
  "numeroPermis": "123456789",
  "typeVehicule": "Moto",
  "plaqueVehicule": "AB-123-CD"
}
```

#### GET `/api/livreurs/profil`
Récupération du profil du livreur connecté (nécessite une session)

#### PUT `/api/livreurs/statut?nouveauStatut=DISPONIBLE`
Modification du statut du livreur (DISPONIBLE, OCCUPE, HORS_SERVICE)

#### GET `/api/livreurs/disponibles`
Liste des livreurs disponibles

#### GET `/api/livreurs/tous`
Liste de tous les livreurs actifs

### 📦 Gestion des Demandes de Livraison

#### POST `/api/demandes-livraison/creer`
Création d'une demande de livraison (commerçant uniquement)
```json
{
  "commandeId": 123,
  "adresseCollecte": "123 Rue du Commerce, 75001 Paris",
  "adresseLivraison": "456 Avenue de la Livraison, 75002 Paris",
  "nomDestinataire": "Martin Durand",
  "telephoneDestinataire": "+33987654321",
  "descriptionColis": "Colis fragile - Produits électroniques",
  "poidsEstime": 2.5,
  "valeurDeclaree": 150.00,
  "fraisLivraison": 15.00,
  "priorite": "NORMALE",
  "dateLivraisonPrevue": "2024-10-15T14:00:00",
  "notesCommercant": "Livraison urgente avant 18h"
}
```

#### POST `/api/demandes-livraison/assigner`
Assignation d'un livreur à une demande (commerçant uniquement)
```json
{
  "demandeId": 1,
  "livreurId": "LIV001",
  "notes": "Livreur expérimenté pour cette zone"
}
```

#### PUT `/api/demandes-livraison/statut`
Modification du statut d'une demande
```json
{
  "demandeId": 1,
  "nouveauStatut": "EN_COLLECTE",
  "notes": "Livreur en route pour la collecte"
}
```

#### GET `/api/demandes-livraison/mes-demandes`
Récupération des demandes selon le rôle :
- **Commerçant** : toutes ses demandes
- **Livreur** : demandes qui lui sont assignées

#### GET `/api/demandes-livraison/mes-livraisons-actives`
Demandes actives du livreur connecté (statuts : ASSIGNEE, ACCEPTEE, EN_COLLECTE, COLLECTEE, EN_LIVRAISON)

#### GET `/api/demandes-livraison/en-attente`
Liste des demandes en attente d'assignation

#### GET `/api/demandes-livraison/{id}`
Détails d'une demande spécifique

#### POST `/api/demandes-livraison/{id}/accepter`
Acceptation d'une demande par le livreur

#### POST `/api/demandes-livraison/{id}/refuser?motif=Indisponible`
Refus d'une demande par le livreur

### 🔐 Authentification

#### POST `/api/auth/connexion`
Connexion avec support des rôles COMMERCANT et LIVREUR
```json
{
  "email": "pierre.martin@commerce.com",
  "motDePasse": "motdepasse123",
  "role": "COMMERCANT"
}
```

```json
{
  "email": "jean.dupont@livreur.com",
  "motDePasse": "motdepasse123",
  "role": "LIVREUR"
}
```

## Workflow Typique

### 1. Inscription et Connexion Commerçant
```bash
# Inscription
POST /api/commercants/inscription

# Connexion
POST /api/auth/connexion
```

### 2. Inscription et Connexion Livreur
```bash
# Inscription
POST /api/livreurs/inscription

# Connexion
POST /api/auth/connexion
```

### 3. Création et Assignation de Demande (Commerçant)
```bash
# Connexion commerçant
POST /api/auth/connexion

# Création demande
POST /api/demandes-livraison/creer

# Voir livreurs disponibles
GET /api/livreurs/disponibles

# Assigner livreur
POST /api/demandes-livraison/assigner
```

### 4. Gestion de Livraison (Livreur)
```bash
# Voir mes demandes
GET /api/demandes-livraison/mes-demandes

# Accepter une demande
POST /api/demandes-livraison/{id}/accepter

# Mettre à jour le statut
PUT /api/demandes-livraison/statut
```

## Statuts et Transitions

### Statuts de Demande
- **EN_ATTENTE** → ASSIGNEE, ANNULEE
- **ASSIGNEE** → ACCEPTEE, REFUSEE, ANNULEE
- **ACCEPTEE** → EN_COLLECTE, ANNULEE
- **EN_COLLECTE** → COLLECTEE, ANNULEE
- **COLLECTEE** → EN_LIVRAISON, ANNULEE
- **EN_LIVRAISON** → LIVREE, ANNULEE

### Statuts de Livreur
- **DISPONIBLE** : Peut recevoir de nouvelles assignations
- **OCCUPE** : En cours de livraison
- **HORS_SERVICE** : Indisponible

## Sécurité

- **Sessions** : Toutes les API nécessitent une session valide
- **Rôles** : 
  - `ROLE_COMMERCANT` : Peut créer et assigner des demandes
  - `ROLE_LIVREUR` : Peut accepter/refuser et mettre à jour ses livraisons
- **Validation** : Tous les inputs sont validés côté serveur

## Tests

Utilisez le script `test-livraison-api.ps1` pour tester toutes les fonctionnalités :

```powershell
.\test-livraison-api.ps1
```

## Base de Données

### Nouvelles Tables
- `livreurs` : Informations des livreurs
- `demandes_delivraison` : Demandes de livraison

### Relations
- `DemandeDelivraison` → `Commercant` (Many-to-One)
- `DemandeDelivraison` → `Livreur` (Many-to-One)
- `DemandeDelivraison` → `Commande` (Many-to-One, optionnel)

## Exemples d'Usage

### Inscription d'un commerçant
```json
{
  "idCommercant": "COM002",
  "nom": "Dubois",
  "prenom": "Sophie",
  "telephone": "+33145678901",
  "email": "sophie.dubois@boutique.fr",
  "motDePasse": "monMotDePasse456",
  "nomBoutique": "Boutique Sophie",
  "adresseBoutique": "789 Boulevard Saint-Germain, 75006 Paris",
  "logoBoutique": "https://example.com/logo.png"
}
```

### Créer une demande de livraison express
```json
{
  "adresseCollecte": "Boutique ABC, 10 Rue de la Paix",
  "adresseLivraison": "15 Avenue des Champs",
  "nomDestinataire": "Marie Martin",
  "telephoneDestinataire": "+33612345678",
  "descriptionColis": "Commande urgente - Médicaments",
  "fraisLivraison": 25.00,
  "priorite": "URGENTE",
  "notesCommercant": "Livraison dans l'heure"
}
```

### Suivre une livraison
```bash
# Livreur accepte
POST /api/demandes-livraison/1/accepter

# En route pour collecte
PUT /api/demandes-livraison/statut
{"demandeId": 1, "nouveauStatut": "EN_COLLECTE"}

# Colis collecté
PUT /api/demandes-livraison/statut
{"demandeId": 1, "nouveauStatut": "COLLECTEE"}

# En livraison
PUT /api/demandes-livraison/statut
{"demandeId": 1, "nouveauStatut": "EN_LIVRAISON"}

# Livré
PUT /api/demandes-livraison/statut
{"demandeId": 1, "nouveauStatut": "LIVREE"}
```
