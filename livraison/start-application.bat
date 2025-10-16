@echo off
title Systeme de Livraison - Port 8000

echo.
echo ========================================
echo    SYSTEME DE LIVRAISON - DEMARRAGE    
echo ========================================
echo.

echo Verification de Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERREUR] Java n'est pas installe ou n'est pas dans le PATH
    pause
    exit /b 1
)
echo [OK] Java detecte

echo Verification de Maven...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERREUR] Maven n'est pas installe ou n'est pas dans le PATH
    pause
    exit /b 1
)
echo [OK] Maven detecte

echo.
echo Compilation de l'application...
call mvn clean compile

if %errorlevel% neq 0 (
    echo [ERREUR] Erreur lors de la compilation
    pause
    exit /b 1
)
echo [OK] Compilation reussie

echo.
echo ========================================
echo    DEMARRAGE SUR LE PORT 8000          
echo ========================================
echo.
echo URLS D'ACCES:
echo -------------
echo Application    : http://localhost:8000
echo Swagger UI     : http://localhost:8000/swagger-ui/index.html
echo API Docs JSON  : http://localhost:8000/api-docs
echo Console H2     : http://localhost:8000/h2-console
echo.
echo ========================================
echo.
echo Appuyez sur Ctrl+C pour arreter l'application
echo.

mvn spring-boot:run
