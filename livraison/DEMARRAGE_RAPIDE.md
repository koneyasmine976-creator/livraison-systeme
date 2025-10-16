# 🚀 Démarrage Rapide - Système de Livraison

## 📋 Prérequis

1. **Java 21+** installé
2. **Maven** installé
3. **MySQL (WAMP)** démarré sur localhost:3306

## ⚡ Démarrage en 3 étapes

### Étape 1 : Télécharger les dépendances
```powershell
mvn clean install -U
```

### Étape 2 : Démarrer l'application
```powershell
mvn spring-boot:run
```

### Étape 3 : Accéder à Swagger
Ouvrez votre navigateur sur :

```
http://localhost:8000/swagger-ui/index.html
```

## 🌐 URLs Importantes

| Service | URL | Description |
|---------|-----|-------------|
| **Swagger UI** | http://localhost:8000/swagger-ui/index.html | Documentation interactive |
| **API Docs JSON** | http://localhost:8000/api-docs | Spécification OpenAPI |
| **Application** | http://localhost:8000 | API REST |
| **H2 Console** | http://localhost:8000/h2-console | Base de données H2 |

## ✅ Vérification

Si tout fonctionne, vous devriez voir :

1. Dans la console :
```
Started LivraisonApplication in X.XXX seconds (JVM running for Y.YYY)
```

2. Dans Swagger UI :
   - Liste des endpoints API
   - Documentation complète
   - Possibilité de tester les API

## ❌ En cas d'erreur

### Erreur : "No static resource swagger-ui"
**Solution** : Utilisez l'URL complète
```
http://localhost:8000/swagger-ui/index.html
```

### Erreur : Port 8000 déjà utilisé
**Solution Windows** :
```powershell
# Trouver le processus
netstat -ano | findstr :8000

# Tuer le processus
taskkill /PID [PID_NUMBER] /F
```

### Erreur : MySQL connection failed
**Solution** : Vérifiez que WAMP/MySQL est démarré

## 🔐 Premiers Tests

### 1. Inscription Commerçant
```bash
POST http://localhost:8000/api/commercants/inscription
Content-Type: application/json

{
  "idCommercant": "COM001",
  "nom": "Martin",
  "prenom": "Pierre",
  "telephone": "+33123456789",
  "email": "pierre.martin@commerce.com",
  "motDePasse": "password123",
  "nomBoutique": "Boutique Martin",
  "adresseBoutique": "123 Rue du Commerce, 75001 Paris"
}
```

### 2. Connexion
```bash
POST http://localhost:8000/api/auth/connexion
Content-Type: application/json

{
  "email": "pierre.martin@commerce.com",
  "motDePasse": "password123",
  "role": "COMMERCANT"
}
```

## 📚 Documentation Complète

- **README_SWAGGER.md** : Guide complet Swagger
- **SWAGGER_DOCUMENTATION.md** : Documentation API détaillée
- **API_LIVRAISON_DOCUMENTATION.md** : Guide des endpoints

## 🆘 Support

En cas de problème :
1. Vérifiez les logs dans la console
2. Consultez les fichiers de documentation
3. Vérifiez que MySQL/WAMP est démarré

---

**Port** : 8000 | **Version** : 1.0.0
