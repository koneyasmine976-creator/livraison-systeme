# Script de configuration automatique de Java 21
# Ce script recherche Java 21 et configure JAVA_HOME

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CONFIGURATION JAVA 21 POUR WINDOWS   " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Recherche de Java 21
Write-Host "Recherche de Java 21 sur votre système..." -ForegroundColor Yellow
Write-Host ""

$possiblePaths = @(
    "C:\Program Files\Eclipse Adoptium\jdk-21*",
    "C:\Program Files\Java\jdk-21*",
    "C:\Program Files\Microsoft\jdk-21*",
    "C:\Program Files\Amazon Corretto\jdk21*"
)

$java21Path = $null
foreach ($pattern in $possiblePaths) {
    $found = Get-ChildItem $pattern -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $java21Path = $found.FullName
        break
    }
}

if ($java21Path) {
    Write-Host "✅ Java 21 trouvé : $java21Path" -ForegroundColor Green
    Write-Host ""
    
    # Vérifier si c'est vraiment Java 21
    $javaExe = Join-Path $java21Path "bin\java.exe"
    if (Test-Path $javaExe) {
        $version = & $javaExe -version 2>&1 | Select-String "version"
        Write-Host "Version détectée : $version" -ForegroundColor Cyan
    }
    
    Write-Host ""
    Write-Host "Configuration de JAVA_HOME..." -ForegroundColor Yellow
    
    # Demander confirmation
    $confirm = Read-Host "Voulez-vous définir JAVA_HOME vers ce chemin? (O/N)"
    
    if ($confirm -eq "O" -or $confirm -eq "o") {
        try {
            # Nécessite des droits administrateur
            Write-Host ""
            Write-Host "⚠️  Cette opération nécessite des droits administrateur" -ForegroundColor Yellow
            Write-Host ""
            
            # Pour la session actuelle
            $env:JAVA_HOME = $java21Path
            $env:PATH = "$java21Path\bin;$env:PATH"
            
            Write-Host "✅ JAVA_HOME configuré pour cette session" -ForegroundColor Green
            Write-Host ""
            Write-Host "JAVA_HOME = $java21Path" -ForegroundColor Cyan
            Write-Host ""
            Write-Host "Pour rendre cette configuration permanente :" -ForegroundColor Yellow
            Write-Host "1. Ouvrez ce script en mode Administrateur" -ForegroundColor White
            Write-Host "2. OU suivez les instructions manuelles dans le guide" -ForegroundColor White
            Write-Host ""
            
            # Vérification
            Write-Host "Vérification de la configuration..." -ForegroundColor Yellow
            java -version
            Write-Host ""
            
            Write-Host "✅ Configuration réussie pour cette session!" -ForegroundColor Green
            Write-Host ""
            Write-Host "Vous pouvez maintenant compiler le projet :" -ForegroundColor Yellow
            Write-Host "   mvn clean install -DskipTests" -ForegroundColor Cyan
            
        } catch {
            Write-Host "❌ Erreur lors de la configuration : $_" -ForegroundColor Red
        }
    } else {
        Write-Host "Configuration annulée." -ForegroundColor Yellow
    }
    
} else {
    Write-Host "❌ Java 21 n'a pas été trouvé sur votre système" -ForegroundColor Red
    Write-Host ""
    Write-Host "Veuillez installer Java 21 :" -ForegroundColor Yellow
    Write-Host "1. Visitez : https://adoptium.net/temurin/releases/?version=21" -ForegroundColor White
    Write-Host "2. Téléchargez Windows x64 JDK" -ForegroundColor White
    Write-Host "3. Installez et relancez ce script" -ForegroundColor White
    Write-Host ""
    
    # Proposer de télécharger
    $download = Read-Host "Voulez-vous ouvrir la page de téléchargement? (O/N)"
    if ($download -eq "O" -or $download -eq "o") {
        Start-Process "https://adoptium.net/temurin/releases/?version=21"
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Read-Host "Appuyez sur Entrée pour continuer"
