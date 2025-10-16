# Script simple pour nettoyer VS Code et forcer Java 21

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   NETTOYAGE VS CODE POUR JAVA 21      " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Nettoyer target
Write-Host "Nettoyage du projet..." -ForegroundColor Yellow
Remove-Item -Path "target" -Recurse -Force -ErrorAction SilentlyContinue
Write-Host "✅ Projet nettoye" -ForegroundColor Green
Write-Host ""

# Configuration Java 21
Write-Host "Configuration Java 21..." -ForegroundColor Yellow
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
Write-Host "✅ Java 21 configure" -ForegroundColor Green
Write-Host ""

# Recompiler
Write-Host "Recompilation..." -ForegroundColor Yellow
mvn clean compile -q
Write-Host "✅ Compilation terminee" -ForegroundColor Green
Write-Host ""

Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "MAINTENANT, dans VS Code :" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Ctrl+Shift+P" -ForegroundColor White
Write-Host "2. Tapez: Java: Clean Java Language Server Workspace" -ForegroundColor White
Write-Host "3. Cliquez sur 'Restart and delete'" -ForegroundColor White
Write-Host "4. Attendez 2-3 minutes" -ForegroundColor White
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Read-Host "Appuyez sur Entree"
