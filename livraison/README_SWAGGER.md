# 🚀 Système de Livraison - Documentation API Swagger

## 📌 Accès Rapide

| Service | URL | Description |
|---------|-----|-------------|
| 🌐 **Application** | http://localhost:8000 | Application principale |
| 📚 **Swagger UI** | http://localhost:8000/swagger-ui.html | Interface interactive API |
| 📄 **API Docs** | http://localhost:8000/api-docs | Documentation JSON |
| 🗄️ **H2 Console** | http://localhost:8000/h2-console | Console base de données |

## 🎯 Démarrage Rapide

### Windows PowerShell
```powershell
.\start-application.ps1
```

### Windows CMD
```batch
start-application.bat
```

### Maven Direct
```bash
mvn spring-boot:run
```

## 📦 Configuration Professionnelle

### ✅ Fonctionnalités Swagger Activées

- **Interface Moderne** : UI Swagger 3.0 avec thème professionnel
- **Documentation Complète** : Tous les endpoints documentés
- **Try it Out** : Test direct des API depuis l'interface
- **Groupes d'API** : Organisation par domaine fonctionnel
- **Authentification** : Support Session & JWT
- **Exemples** : Requêtes et réponses pré-remplies
- **Validation** : Schémas de données avec contraintes
- **Métriques** : Temps de réponse et taille des données

## 🔐 Authentification

### 1. Créer un compte

#### Commerçant
```json
POST http://localhost:8000/api/commercants/inscription
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

#### Livreur
```json
POST http://localhost:8000/api/livreurs/inscription
{
  "idLivreur": "LIV001",
  "nom": "Dupont",
  "prenom": "Jean",
  "telephone": "+33123456789",
  "email": "jean.dupont@livreur.com",
  "motDePasse": "password123",
  "numeroPermis": "123456789",
  "typeVehicule": "Moto",
  "plaqueVehicule": "AB-123-CD"
}
```

### 2. Se connecter
```json
POST http://localhost:8000/api/auth/connexion
{
  "email": "votre.email@example.com",
  "motDePasse": "votre_mot_de_passe",
  "role": "COMMERCANT"  // ou "LIVREUR" ou "CLIENT"
}
```

## 📊 Groupes d'API

### 🌐 API Publique
- `/api/auth/**` - Authentification
- `/api/*/inscription` - Inscriptions

### 🏪 API Commerçant
- `/api/commercants/**` - Gestion boutique
- `/api/demandes-livraison/**` - Demandes de livraison
- `/api/produits/**` - Catalogue produits

### 🚚 API Livreur
- `/api/livreurs/**` - Profil et statuts
- `/api/demandes-livraison/**` - Livraisons assignées

### 👤 API Client
- `/api/clients/**` - Profil client
- `/api/commandes/**` - Commandes
- `/api/adresses/**` - Adresses

## 🎨 Interface Swagger

### Navigation
1. **Accédez à** : http://localhost:8000/swagger-ui.html
2. **Sélectionnez un groupe** : Public, Commerçant, Livreur, Client
3. **Explorez les endpoints** : Cliquez pour voir les détails
4. **Testez** : Utilisez "Try it out" pour tester

### Fonctionnalités Avancées
- 🔍 **Recherche** : Barre de recherche globale
- 🏷️ **Filtrage** : Par tags et méthodes HTTP
- 📋 **Export** : Téléchargez la documentation
- 🎯 **Exemples** : Requêtes pré-remplies

## 📈 Monitoring et Métriques

### Endpoints de Santé
- `/actuator/health` - État de l'application
- `/actuator/metrics` - Métriques détaillées
- `/actuator/info` - Informations système

## 🛠️ Dépannage

### Problème : Swagger ne s'affiche pas
```bash
# Vérifiez que l'application est démarrée
curl http://localhost:8000/api-docs

# Vérifiez les logs
mvn spring-boot:run | grep -i swagger
```

### Problème : Erreur 403 Forbidden
- Les endpoints Swagger sont publics
- Vérifiez SecurityConfig.java
- Redémarrez l'application

### Problème : Port 8000 occupé
```bash
# Windows - Trouver le processus
netstat -ano | findstr :8000

# Tuer le processus
taskkill /PID [PID_NUMBER] /F
```

## 📝 Export de la Documentation

### Format JSON
```bash
curl http://localhost:8000/api-docs > api-docs.json
```

### Format YAML
```bash
curl http://localhost:8000/api-docs.yaml > api-docs.yaml
```

### Import Postman
1. Ouvrez Postman
2. Import → Link
3. Collez : `http://localhost:8000/api-docs`

## 🌟 Points Forts

✅ **Port 8000** configuré comme demandé
✅ **Documentation professionnelle** complète
✅ **Interface moderne** et intuitive
✅ **Sécurité** intégrée
✅ **Multi-environnement** supporté
✅ **Performance** optimisée

## 📞 Support

Pour toute question sur l'API :
- 📧 Email : support@livraison-system.com
- 📚 Documentation : http://localhost:8000/swagger-ui.html
- 🐛 Issues : GitHub Issues

---

**Version** : 1.0.0 | **Port** : 8000 | **Environnement** : Développement
