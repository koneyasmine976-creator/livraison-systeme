# Script de démarrage professionnel pour l'application de livraison
# Port configuré : 8000

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   SYSTÈME DE LIVRAISON - DÉMARRAGE    " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Vérification de Java
Write-Host "📋 Vérification de l'environnement..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version"
if ($javaVersion) {
    Write-Host "✅ Java détecté: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "❌ Java n'est pas installé ou n'est pas dans le PATH" -ForegroundColor Red
    exit 1
}

# Vérification de Maven
$mvnVersion = mvn -version 2>&1 | Select-String "Apache Maven"
if ($mvnVersion) {
    Write-Host "✅ Maven détecté: $mvnVersion" -ForegroundColor Green
} else {
    Write-Host "❌ Maven n'est pas installé ou n'est pas dans le PATH" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🔧 Compilation de l'application..." -ForegroundColor Yellow
mvn clean compile

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilation réussie" -ForegroundColor Green
} else {
    Write-Host "❌ Erreur lors de la compilation" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🚀 Démarrage de l'application sur le port 8000..." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   URLS D'ACCÈS DISPONIBLES            " -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "📱 Application    : http://localhost:8000" -ForegroundColor White
Write-Host "📚 Swagger UI     : http://localhost:8000/swagger-ui/index.html" -ForegroundColor White
Write-Host "📄 API Docs JSON  : http://localhost:8000/api-docs" -ForegroundColor White
Write-Host "🗄️  Console H2     : http://localhost:8000/h2-console" -ForegroundColor White
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "💡 Appuyez sur Ctrl+C pour arrêter l'application" -ForegroundColor Yellow
Write-Host ""

# Démarrage de l'application
mvn spring-boot:run
