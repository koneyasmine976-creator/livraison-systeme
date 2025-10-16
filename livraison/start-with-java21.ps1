# Script de démarrage avec Java 21 forcé
# Ignore les erreurs de l'IDE et utilise Java 21

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   DEMARRAGE AVEC JAVA 21 FORCE        " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Forcer Java 21
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"

Write-Host "Configuration Java..." -ForegroundColor Yellow
Write-Host "JAVA_HOME = $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host ""

# Vérifier Java
Write-Host "Verification de la version Java..." -ForegroundColor Yellow
java -version
Write-Host ""

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erreur : Java 21 introuvable" -ForegroundColor Red
    Write-Host "Verifiez que Java 21 est installe dans :" -ForegroundColor Yellow
    Write-Host "  $javaHome" -ForegroundColor White
    exit 1
}

Write-Host "✅ Java 21 detecte et configure" -ForegroundColor Green
Write-Host ""

# Vérifier MySQL
Write-Host "⚠️  Verifiez que MySQL/WAMP est demarre" -ForegroundColor Yellow
Write-Host ""

# Démarrer l'application
Write-Host "🚀 Demarrage de l'application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   URLS DISPONIBLES SUR PORT 8000      " -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "📚 Swagger UI     : http://localhost:8000/swagger-ui/index.html" -ForegroundColor White
Write-Host "📄 API Docs       : http://localhost:8000/api-docs" -ForegroundColor White
Write-Host "🌐 Application    : http://localhost:8000" -ForegroundColor White
Write-Host "🗄️  H2 Console     : http://localhost:8000/h2-console" -ForegroundColor White
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "💡 Appuyez sur Ctrl+C pour arreter" -ForegroundColor Yellow
Write-Host ""

# Lancer
mvn spring-boot:run
