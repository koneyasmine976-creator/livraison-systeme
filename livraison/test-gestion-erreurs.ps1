# Script de test pour démontrer la gestion des erreurs et des messages de succès
Write-Host "=== Test de la Gestion des Erreurs et Messages ===" -ForegroundColor Green

# Attendre que l'application démarre
Write-Host "`nAttente du démarrage de l'application..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Test 1: Inscription client réussie
Write-Host "`n1. Test d'inscription client réussie..." -ForegroundColor Cyan
$clientData = @{
    idClient = "CLI001"
    nom = "Dupont"
    prenom = "Jean"
    email = "jean.dupont@email.com"
    telephone = "+33123456789"
    motDePasse = "motdepasse123"
    adresse = "123 Rue de la Paix, Paris, France"
    paysResidence = "France"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/client/inscription" -Method POST -ContentType "application/json" -Body $clientData
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Tentative d'inscription avec email existant (doit échouer)
Write-Host "`n2. Test d'inscription avec email existant (doit échouer)..." -ForegroundColor Cyan
$clientData2 = @{
    idClient = "CLI002"
    nom = "Martin"
    prenom = "Pierre"
    email = "jean.dupont@email.com"  # Email déjà utilisé
    telephone = "+33987654321"
    motDePasse = "motdepasse123"
    adresse = "456 Rue de la République, Lyon, France"
    paysResidence = "France"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/client/inscription" -Method POST -ContentType "application/json" -Body $clientData2
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur attendue: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Test 3: Connexion réussie
Write-Host "`n3. Test de connexion réussie..." -ForegroundColor Cyan
$loginData = @{
    email = "jean.dupont@email.com"
    motDePasse = "motdepasse123"
    role = "CLIENT"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginData
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Tentative de connexion avec mot de passe incorrect
Write-Host "`n4. Test de connexion avec mot de passe incorrect (doit échouer)..." -ForegroundColor Cyan
$loginData2 = @{
    email = "jean.dupont@email.com"
    motDePasse = "mauvais_mot_de_passe"
    role = "CLIENT"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginData2
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur attendue: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Test 5: Tentative de connexion avec rôle invalide
Write-Host "`n5. Test de connexion avec rôle invalide (doit échouer)..." -ForegroundColor Cyan
$loginData3 = @{
    email = "jean.dupont@email.com"
    motDePasse = "motdepasse123"
    role = "ADMIN"  # Rôle invalide
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginData3
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur attendue: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Test 6: Inscription commerçant réussie
Write-Host "`n6. Test d'inscription commerçant réussie..." -ForegroundColor Cyan
$commercantData = @{
    idCommercant = "COM001"
    nom = "Martin"
    prenom = "Sophie"
    email = "sophie.martin@boutique.com"
    telephone = "+33987654321"
    motDePasse = "motdepasse123"
    nomBoutique = "Boutique Sophie"
    adresseBoutique = "456 Avenue des Champs, Paris, France"
    logoBoutique = "https://example.com/logo.png"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/commercant/inscription" -Method POST -ContentType "application/json" -Body $commercantData
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Connexion commerçant
Write-Host "`n7. Test de connexion commerçant..." -ForegroundColor Cyan
$loginCommercant = @{
    email = "sophie.martin@boutique.com"
    motDePasse = "motdepasse123"
    role = "COMMERCANT"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginCommercant
    Write-Host "✅ Succès: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Tests terminés ===" -ForegroundColor Green
Write-Host "Les messages d'erreur et de succès sont maintenant plus clairs et informatifs !" -ForegroundColor Yellow
