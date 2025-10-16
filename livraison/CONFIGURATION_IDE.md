# 🔧 Configuration IDE pour Java 21

## ⚠️ Problème
Votre IDE affiche `java.lang.ExceptionInInitializerError` car il utilise **Java 25** au lieu de **Java 21**.

Maven compile correctement, mais l'IDE doit aussi être configuré.

---

## 📝 Configuration VS Code

### Méthode 1 : Via l'Interface (Recommandé)

1. **Ouvrez VS Code** dans le projet
2. Appuyez sur **Ctrl+Shift+P**
3. Tapez : `Java: Configure Java Runtime`
4. Dans la section **"Java Tooling Runtime"**, sélectionnez **Java 21**
5. Dans la section **"Java Project Runtime"**, pour votre projet, sélectionnez **JavaSE-21**

### Méthode 2 : Fichier de Configuration

1. Créez le dossier `.vscode` à la racine si non existant :
   ```powershell
   New-Item -ItemType Directory -Force -Path .vscode
   ```

2. Créez `.vscode/settings.json` avec ce contenu :
   ```json
   {
       "java.configuration.runtimes": [
           {
               "name": "JavaSE-21",
               "path": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.8.9-hotspot",
               "default": true
           }
       ],
       "java.jdt.ls.java.home": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.8.9-hotspot",
       "maven.terminal.customEnv": [
           {
               "environmentVariable": "JAVA_HOME",
               "value": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.8.9-hotspot"
           }
       ]
   }
   ```

3. **Redémarrez VS Code** complètement

4. Nettoyez le workspace Java :
   - **Ctrl+Shift+P** → `Java: Clean Java Language Server Workspace`
   - Confirmez

---

## 🔷 Configuration IntelliJ IDEA

### Étape 1 : Définir le JDK du Projet

1. **File** → **Project Structure** (ou **Ctrl+Alt+Shift+S**)
2. Dans **Project** :
   - **SDK** : Sélectionnez **21** (Eclipse Adoptium 21.0.8)
   - Si absent, cliquez **Add SDK** → **Download JDK** ou **Add SDK** → **JDK** et pointez vers :
     ```
     C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
     ```
   - **Language level** : **21 - Pattern matching for switch**
3. Cliquez **Apply** et **OK**

### Étape 2 : Définir le JDK des Modules

1. Toujours dans **Project Structure**
2. Allez dans **Modules**
3. Sélectionnez votre module **livraison**
4. Onglet **Dependencies**
5. **Module SDK** : Sélectionnez **Project SDK (21)**
6. Cliquez **Apply** et **OK**

### Étape 3 : Configurer Maven

1. **File** → **Settings** (ou **Ctrl+Alt+S**)
2. **Build, Execution, Deployment** → **Build Tools** → **Maven**
3. **Maven home path** : Vérifiez qu'il pointe vers votre Maven
4. **JDK for importer** : Sélectionnez **Use Project JDK (21)**
5. **Runner** :
   - **JRE** : Sélectionnez **Use Project JDK (21)**
6. Cliquez **Apply** et **OK**

### Étape 4 : Recharger le Projet

1. Clic droit sur **pom.xml**
2. **Maven** → **Reload Project**
3. Attendez la fin de l'indexation

### Étape 5 : Invalider le Cache

1. **File** → **Invalidate Caches / Restart**
2. Cochez **Clear file system cache and Local History**
3. Cliquez **Invalidate and Restart**

---

## ✅ Vérification

Après configuration de l'IDE :

### Dans VS Code
```
Ctrl+Shift+P → Java: Show Build Job Status
```
Vérifiez qu'il n'y a plus d'erreurs de build.

### Dans IntelliJ IDEA
```
File → Project Structure → Project → SDK
```
Doit afficher : **21 (Eclipse Adoptium 21.0.8)**

### Test de Compilation
Dans le terminal intégré de l'IDE :
```powershell
mvn clean compile
```
Doit réussir sans erreur.

---

## 🚀 Script Rapide de Configuration

Pour automatiser la configuration Java 21 dans l'environnement :

```powershell
# Définir JAVA_HOME pour la session actuelle
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Vérifier
java -version

# Nettoyer et recompiler
mvn clean compile
```

---

## 🔍 Diagnostic

### Vérifier quelle version Java l'IDE utilise

#### VS Code
1. **Ctrl+Shift+P** → `Java: Open Java Language Server Log File`
2. Recherchez "Using Java" → doit afficher Java 21

#### IntelliJ IDEA
1. **Help** → **About**
2. Copiez les infos et vérifiez la JRE version
3. Ou : **File** → **Project Structure** → **Project** → Vérifiez **SDK**

---

## ⚡ Solution Rapide

Si vous n'arrivez pas à configurer l'IDE, compilez et exécutez via le terminal :

```powershell
# Dans le dossier du projet
cd c:\Users\junior\Desktop\livraison-systeme\livraison

# Définir Java 21
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Lancer l'application
mvn spring-boot:run
```

L'application démarrera correctement même si l'IDE affiche des erreurs !

---

## 📞 Support

Si les erreurs persistent :
1. ✅ Le build Maven réussit → **Utilisez Maven pour démarrer**
2. ❌ L'IDE affiche des erreurs → **C'est juste l'IDE, l'app fonctionne**

### Commande de Démarrage Garantie
```powershell
# Cette commande fonctionne toujours
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"; $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"; mvn spring-boot:run
```

---

**Note** : Les erreurs dans l'IDE sont visuelles. Si Maven compile, l'application fonctionne parfaitement ! ✅
