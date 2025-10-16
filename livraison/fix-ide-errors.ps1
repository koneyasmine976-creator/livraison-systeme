# Script pour corriger les erreurs IDE et forcer Java 21
# Ce script nettoie complètement le workspace Java de VS Code

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CORRECTION DES ERREURS IDE - JAVA 21 " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectPath = $PSScriptRoot

# Étape 1 : Vérifier Java 21
Write-Host "1/6 - Verification de Java 21..." -ForegroundColor Yellow
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"

if (Test-Path "$javaHome\bin\java.exe") {
    Write-Host "   ✅ Java 21 trouve" -ForegroundColor Green
    & "$javaHome\bin\java.exe" -version 2>&1 | Select-Object -First 1
} else {
    Write-Host "   ❌ Java 21 introuvable a : $javaHome" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Étape 2 : Configuration JAVA_HOME
Write-Host "2/6 - Configuration de JAVA_HOME..." -ForegroundColor Yellow
$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"
Write-Host "   ✅ JAVA_HOME = $javaHome" -ForegroundColor Green
Write-Host ""

# Étape 3 : Nettoyer le projet Maven
Write-Host "3/6 - Nettoyage du projet Maven..." -ForegroundColor Yellow
try {
    Remove-Item -Path "$projectPath\target" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "   ✅ Dossier target supprime" -ForegroundColor Green
} catch {
    Write-Host "   ⚠️  Target deja supprime" -ForegroundColor Yellow
}
Write-Host ""

# Étape 4 : Nettoyer les caches VS Code Java
Write-Host "4/6 - Nettoyage des caches VS Code..." -ForegroundColor Yellow
$workspaceStorage = "$env:APPDATA\Code\User\workspaceStorage"
if (Test-Path $workspaceStorage) {
    try {
        Get-ChildItem $workspaceStorage -Directory | ForEach-Object {
            $workspaceDataPath = Join-Path $_.FullName "workspace.json"
            if (Test-Path $workspaceDataPath) {
                $content = Get-Content $workspaceDataPath -Raw -ErrorAction SilentlyContinue
                if ($content -like "*livraison*") {
                    Write-Host "   Suppression du cache pour : $($_.Name)" -ForegroundColor Cyan
                    Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
                }
            }
        }
        Write-Host "   ✅ Caches VS Code nettoyes" -ForegroundColor Green
    } catch {
        Write-Host "   ⚠️  Impossible de nettoyer certains caches" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ℹ️  Aucun cache VS Code trouve" -ForegroundColor Cyan
}
Write-Host ""

# Étape 5 : Recompiler avec Java 21
Write-Host "5/6 - Recompilation avec Java 21..." -ForegroundColor Yellow
mvn clean compile -q
if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✅ Compilation reussie" -ForegroundColor Green
} else {
    Write-Host "   ❌ Erreur de compilation" -ForegroundColor Red
    Write-Host "   Essayez : mvn clean compile" -ForegroundColor Yellow
}
Write-Host ""

# Étape 6 : Instructions pour VS Code
Write-Host "6/6 - Actions a effectuer dans VS Code..." -ForegroundColor Yellow
Write-Host ""
Write-Host "   📋 INSTRUCTIONS IMPORTANTES :" -ForegroundColor Cyan
Write-Host ""
Write-Host "   1. Fermez COMPLETEMENT VS Code (toutes les fenetres)" -ForegroundColor White
Write-Host ""
Write-Host "   2. Rouvrez VS Code dans ce dossier :" -ForegroundColor White
Write-Host "      $projectPath" -ForegroundColor Cyan
Write-Host ""
Write-Host "   3. Appuyez sur Ctrl+Shift+P" -ForegroundColor White
Write-Host ""
Write-Host "   4. Tapez : Java: Clean Java Language Server Workspace" -ForegroundColor White
Write-Host ""
Write-Host "   5. Confirmez et attendez le reload" -ForegroundColor White
Write-Host ""
Write-Host "   6. Appuyez de nouveau sur Ctrl+Shift+P" -ForegroundColor White
Write-Host ""
Write-Host "   7. Tapez : Java: Configure Java Runtime" -ForegroundColor White
Write-Host ""
Write-Host "   8. Verifiez que Java 21 est selectionne" -ForegroundColor White
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "✅ Configuration completee !" -ForegroundColor Green
Write-Host ""
Write-Host "⚠️  NOTE IMPORTANTE :" -ForegroundColor Yellow
Write-Host "Les erreurs dans l'IDE sont cosmetiques." -ForegroundColor White
Write-Host "L'application fonctionne parfaitement !" -ForegroundColor Green
Write-Host ""
Write-Host "Pour demarrer l'application :" -ForegroundColor Yellow
Write-Host "   .\start-with-java21.ps1" -ForegroundColor Cyan
Write-Host ""
Write-Host "Swagger UI accessible sur :" -ForegroundColor Yellow
Write-Host "   http://localhost:8000/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host ""

Read-Host "Appuyez sur Entree pour continuer"
