# Script de correction automatique de toutes les erreurs
# Ce script nettoie et recompile le projet avec Lombok

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CORRECTION AUTOMATIQUE DES ERREURS   " -ForegroundColor Cyan  
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectPath = $PSScriptRoot

# Étape 1 : Nettoyage complet
Write-Host "🧹 Étape 1/5 : Nettoyage complet du projet..." -ForegroundColor Yellow
try {
    Remove-Item -Path "$projectPath\target" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "   ✅ Dossier target supprimé" -ForegroundColor Green
} catch {
    Write-Host "   ⚠️  Impossible de supprimer target (peut-être déjà supprimé)" -ForegroundColor Yellow
}

# Étape 2 : Maven clean
Write-Host ""
Write-Host "🧹 Étape 2/5 : Maven clean..." -ForegroundColor Yellow
mvn clean
if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✅ Maven clean réussi" -ForegroundColor Green
} else {
    Write-Host "   ❌ Erreur Maven clean" -ForegroundColor Red
    exit 1
}

# Étape 3 : Nettoyage cache Lombok (optionnel)
Write-Host ""
Write-Host "🧹 Étape 3/5 : Nettoyage cache Lombok..." -ForegroundColor Yellow
$lombokCache = "$env:USERPROFILE\.m2\repository\org\projectlombok\lombok"
if (Test-Path $lombokCache) {
    try {
        Remove-Item -Path $lombokCache -Recurse -Force
        Write-Host "   ✅ Cache Lombok nettoyé" -ForegroundColor Green
    } catch {
        Write-Host "   ⚠️  Impossible de nettoyer le cache Lombok" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ℹ️  Pas de cache Lombok à nettoyer" -ForegroundColor Cyan
}

# Étape 4 : Télécharger les dépendances
Write-Host ""
Write-Host "📦 Étape 4/5 : Téléchargement des dépendances..." -ForegroundColor Yellow
mvn dependency:resolve -U
if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✅ Dépendances téléchargées" -ForegroundColor Green
} else {
    Write-Host "   ❌ Erreur téléchargement dépendances" -ForegroundColor Red
    exit 1
}

# Étape 5 : Compilation complète
Write-Host ""
Write-Host "🔨 Étape 5/5 : Compilation du projet..." -ForegroundColor Yellow
Write-Host "   (Cela peut prendre quelques minutes...)" -ForegroundColor Cyan
mvn clean install -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "         ✅ SUCCÈS !                    " -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Le projet a été compilé avec succès !" -ForegroundColor Green
    Write-Host ""
    Write-Host "Prochaines étapes :" -ForegroundColor Yellow
    Write-Host "1. Redémarrez votre IDE (VS Code/Eclipse/IntelliJ)" -ForegroundColor White
    Write-Host "2. Laissez l'IDE réindexer le projet" -ForegroundColor White
    Write-Host "3. Les erreurs 'cannot find symbol' devraient disparaître" -ForegroundColor White
    Write-Host ""
    Write-Host "Pour démarrer l'application :" -ForegroundColor Yellow
    Write-Host "   mvn spring-boot:run" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Pour accéder à Swagger :" -ForegroundColor Yellow
    Write-Host "   http://localhost:8000/swagger-ui/index.html" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "         ❌ ÉCHEC                       " -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "La compilation a échoué." -ForegroundColor Red
    Write-Host ""
    Write-Host "Actions recommandées :" -ForegroundColor Yellow
    Write-Host "1. Vérifiez que Java 21 est installé : java -version" -ForegroundColor White
    Write-Host "2. Vérifiez que Maven est installé : mvn -version" -ForegroundColor White
    Write-Host "3. Consultez le fichier RESOLUTION_ERREURS.md" -ForegroundColor White
    Write-Host "4. Vérifiez les logs d'erreur ci-dessus" -ForegroundColor White
    Write-Host ""
    exit 1
}

Read-Host "Appuyez sur Entrée pour continuer"
