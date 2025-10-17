# 🔧 Configuration IntelliJ IDEA pour Java 21

## ⚠️ Problème
IntelliJ utilise Java 25 au lieu de Java 21, causant les erreurs :
```
❌ java.lang.ExceptionInInitializerError
❌ com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

---

## ✅ Solution Rapide (5 minutes)

### Étape 1 : Configurer le SDK du Projet

1. Ouvrez IntelliJ IDEA avec votre projet
2. **File** → **Project Structure** (ou **Ctrl+Alt+Shift+S**)
3. Dans **Project** (à gauche) :
   - **SDK** : Cliquez sur le menu déroulant
   - Si "21" est dans la liste : Sélectionnez **21 (Eclipse Adoptium 21.0.8)**
   - Sinon : Cliquez sur **Add SDK** → **JDK** → Naviguez vers :
     ```
     C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
     ```
   - **Language level** : Sélectionnez **21 - Pattern matching for switch**
4. Cliquez **Apply**

### Étape 2 : Configurer Maven pour Java 21

1. Toujours dans **Project Structure** (ou fermez et rouvrez)
2. **File** → **Settings** (ou **Ctrl+Alt+S**)
3. Naviguez vers : **Build, Execution, Deployment** → **Build Tools** → **Maven**
4. **Maven home path** : Vérifiez qu'il pointe vers Maven
5. **JDK for importer** : Sélectionnez **Use Project JDK** ou **21**
6. Allez dans **Maven** → **Runner** :
   - **JRE** : Sélectionnez **Use Project JDK** ou **21**
7. Cliquez **Apply** et **OK**

### Étape 3 : Recharger le Projet Maven

1. Ouvrez l'onglet **Maven** (côté droit de l'IDE)
2. Cliquez sur l'icône 🔄 **Reload All Maven Projects** (en haut de l'onglet Maven)
3. Attendez que le reload se termine (1-2 minutes)

### Étape 4 : Invalider les Caches

1. **File** → **Invalidate Caches / Restart**
2. Cochez ces options :
   - ☑ **Clear file system cache and Local History**
   - ☑ **Clear downloaded shared indexes**
   - ☑ **Clear VCS Log caches and indexes**
3. Cliquez sur **Invalidate and Restart**
4. IntelliJ va redémarrer

### Étape 5 : Rebuild du Projet

Après le redémarrage :

1. **Build** → **Rebuild Project**
2. Attendez que le build se termine

------

## 📊 Vérification

### Vérifier le SDK
1. **File** → **Project Structure** → **Project**
2. Doit afficher : **SDK: 21 (Eclipse Adoptium 21.0.8)**

### Vérifier Maven
1. **File** → **Settings** → **Build, Execution, Deployment** → **Maven** → **Runner**
2. **JRE** doit afficher : **Use Project JDK (21)**

### Vérifier la Compilation
1. Ouvrez le terminal IntelliJ (en bas)
2. Exécutez :
   ```powershell
   mvn clean compile
   ```
3. Doit afficher : **BUILD SUCCESS**

---

## 🎯 Si les Erreurs Persistent

### Option 1 : Clean and Rebuild
```
Build → Clean Project
Build → Rebuild Project
```

### Option 2 : Supprimer les Caches Manuellement

1. Fermez IntelliJ complètement
2. Supprimez ces dossiers :
   ```powershell
   Remove-Item -Path ".idea" -Recurse -Force
   Remove-Item -Path "target" -Recurse -Force
   Remove-Item -Path "$env:USERPROFILE\.IntelliJIdea*\system\compile-server" -Recurse -Force
   ```
3. Rouvrez le projet
4. **File** → **Invalidate Caches / Restart**

### Option 3 : Réimporter le Projet

1. Fermez IntelliJ
2. Supprimez le dossier `.idea`
3. Rouvrez IntelliJ
4. **File** → **Open** → Sélectionnez `pom.xml`
5. Choisissez **Open as Project**
6. Laissez IntelliJ importer le projet (5-10 minutes)

---

## 🚀 Démarrage de l'Application depuis IntelliJ

### Option 1 : Maven
1. Onglet **Maven** (à droite)
2. **livraison** → **Plugins** → **spring-boot** → **spring-boot:run**
3. Double-cliquez

### Option 2 : Configuration Run
1. **Run** → **Edit Configurations**
2. Cliquez sur **+** → **Maven**
3. **Name** : "Spring Boot Run"
4. **Command line** : `spring-boot:run`
5. **Apply** et **OK**
6. Cliquez sur le bouton ▶️ **Run**

### Option 3 : Main Class
1. Ouvrez `LivraisonApplication.java`
2. Cliquez sur ▶️ vert à côté de la classe
3. **Run 'LivraisonApplication'**

---

## 📦 Configuration Automatique pour IntelliJ

Créez un fichier `.idea/misc.xml` avec :
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectRootManager" version="2" languageLevel="JDK_21" default="true" project-jdk-name="21" project-jdk-type="JavaSDK">
    <output url="file://$PROJECT_DIR$/out" />
  </component>
</project>
```

---

## ✅ Checklist de Configuration

- [ ] Project Structure → SDK = 21
- [ ] Project Structure → Language Level = 21
- [ ] Settings → Maven → JDK for importer = Project JDK
- [ ] Settings → Maven → Runner → JRE = Project JDK
- [ ] Maven → Reload All Projects
- [ ] File → Invalidate Caches / Restart
- [ ] Build → Rebuild Project

---

## 🎉 Résultat Final

Après ces étapes :
- ✅ Aucune erreur rouge dans l'IDE
- ✅ Compilation réussie
- ✅ Application démarrable depuis IntelliJ
- ✅ Tous les outils IntelliJ fonctionnels

---

## 💡 Note Importante

**L'application fonctionne déjà parfaitement avec Maven !**

Ces étapes sont uniquement pour corriger l'affichage dans IntelliJ.

Si vous voulez juste démarrer l'application maintenant :
```powershell
.\start-with-java21.ps1
```

L'application sera accessible sur :
- **Swagger UI** : http://localhost:8000/swagger-ui/index.html

---

## 🆘 Support Rapide

Si un problème persiste après toutes ces étapes :

1. Fermez IntelliJ
2. Exécutez depuis le terminal :
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
   mvn spring-boot:run
   ```
3. L'application démarre sans problème

**Les erreurs IntelliJ n'affectent pas l'exécution !**
