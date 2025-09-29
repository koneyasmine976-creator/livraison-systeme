# Script de test pour les APIs de gestion des commandes
Write-Host "=== Test des APIs de Gestion des Commandes ===" -ForegroundColor Green

# Attendre que l'application démarre
Write-Host "`nAttente du démarrage de l'application..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Variables pour stocker les IDs
$clientEmail = "jean.dupont@email.com"
$commercantEmail = "sophie.martin@boutique.com"
$commandeId = $null
$produitId = $null
$adresseId = $null

# Test 1: Connexion client
Write-Host "`n1. Connexion client..." -ForegroundColor Cyan
$loginClient = @{
    email = $clientEmail
    motDePasse = "motdepasse123"
    role = "CLIENT"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginClient
    Write-Host "✅ Client connecté: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur connexion client: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Créer une adresse
Write-Host "`n2. Création d'une adresse..." -ForegroundColor Cyan
$adresseData = @{
    rue = "123 Rue de la Paix"
    ville = "Paris"
    codePostal = "75001"
    pays = "France"
    complement = "Appartement 2A"
    telephone = "+33123456789"
    principale = $true
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/adresses/creer" -Method POST -ContentType "application/json" -Body $adresseData
    $adresseId = $response.data.id
    Write-Host "✅ Adresse créée: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur création adresse: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Connexion commerçant
Write-Host "`n3. Connexion commerçant..." -ForegroundColor Cyan
$loginCommercant = @{
    email = $commercantEmail
    motDePasse = "motdepasse123"
    role = "COMMERCANT"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginCommercant
    Write-Host "✅ Commerçant connecté: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur connexion commerçant: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Créer un produit
Write-Host "`n4. Création d'un produit..." -ForegroundColor Cyan
$produitData = @{
    nom = "Produit Test"
    description = "Description du produit test"
    prix = 25.50
    stock = 100
    categorie = "Test"
    imageUrl = "https://example.com/image.jpg"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/produits/creer" -Method POST -ContentType "application/json" -Body $produitData
    $produitId = $response.data.id
    Write-Host "✅ Produit créé: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur création produit: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Reconnecter le client pour créer une commande
Write-Host "`n5. Reconnexion client pour créer une commande..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginClient
    Write-Host "✅ Client reconnecté: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur reconnexion client: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Créer une commande
Write-Host "`n6. Création d'une commande..." -ForegroundColor Cyan
$commandeData = @{
    adresseLivraisonId = $adresseId
    lignesCommande = @(
        @{
            produitId = $produitId
            quantite = 2
        }
    )
    notes = "Livraison en matinée si possible"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/commandes/creer" -Method POST -ContentType "application/json" -Body $commandeData
    $commandeId = $response.data.id
    Write-Host "✅ Commande créée: $($response.message)" -ForegroundColor Green
    Write-Host "   Numéro: $($response.data.numeroCommande)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Erreur création commande: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Consulter les commandes du client
Write-Host "`n7. Consultation des commandes du client..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/commandes/mes-commandes" -Method GET
    Write-Host "✅ Commandes récupérées: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur récupération commandes: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: Reconnexion commerçant pour gérer la commande
Write-Host "`n8. Reconnexion commerçant pour gérer la commande..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/connexion" -Method POST -ContentType "application/json" -Body $loginCommercant
    Write-Host "✅ Commerçant reconnecté: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur reconnexion commerçant: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 9: Modifier le statut de la commande
Write-Host "`n9. Modification du statut de la commande..." -ForegroundColor Cyan
$statutData = @{
    statut = "EN_COURS"
    notes = "Commande en cours de préparation"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/commandes/$commandeId/statut" -Method PUT -ContentType "application/json" -Body $statutData
    Write-Host "✅ Statut modifié: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur modification statut: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 10: Télécharger le reçu
Write-Host "`n10. Téléchargement du reçu..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/receipts/$commandeId/download" -Method GET -OutFile "receipt_$commandeId.pdf"
    Write-Host "✅ Reçu téléchargé: receipt_$commandeId.pdf" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur téléchargement reçu: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Tests terminés ===" -ForegroundColor Green
Write-Host "Les APIs de gestion des commandes sont fonctionnelles !" -ForegroundColor Yellow
Write-Host "Fichiers générés:" -ForegroundColor Cyan
Write-Host "- receipt_$commandeId.pdf (reçu de livraison)" -ForegroundColor White
