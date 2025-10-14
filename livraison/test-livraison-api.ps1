# Script de test pour les nouvelles API de livraison
# Assurez-vous que l'application Spring Boot est démarrée avant d'exécuter ce script

$baseUrl = "http://localhost:8080/api"
$headers = @{
    "Content-Type" = "application/json"
}

Write-Host "=== Test des API de Livraison ===" -ForegroundColor Green

# 1. Test d'inscription d'un livreur
Write-Host "`n1. Test d'inscription d'un livreur..." -ForegroundColor Yellow

$livreurData = @{
    idLivreur = "LIV001"
    nom = "Dupont"
    prenom = "Jean"
    telephone = "+33123456789"
    email = "jean.dupont@livreur.com"
    motDePasse = "motdepasse123"
    numeroPermis = "123456789"
    typeVehicule = "Moto"
    plaqueVehicule = "AB-123-CD"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/livreurs/inscription" -Method POST -Body $livreurData -Headers $headers
    Write-Host "✓ Inscription livreur réussie: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur inscription livreur: $($_.Exception.Message)" -ForegroundColor Red
}

# 2. Test de connexion du livreur
Write-Host "`n2. Test de connexion du livreur..." -ForegroundColor Yellow

$connexionData = @{
    email = "jean.dupont@livreur.com"
    motDePasse = "motdepasse123"
    role = "LIVREUR"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/connexion" -Method POST -Body $connexionData -Headers $headers -SessionVariable session
    $responseData = $response.Content | ConvertFrom-Json
    Write-Host "✓ Connexion livreur réussie: $($responseData.message)" -ForegroundColor Green
    
    # Extraire les cookies de session pour les requêtes suivantes
    $sessionCookie = $session.Cookies.GetCookies("$baseUrl")
    $cookieHeader = @{
        "Content-Type" = "application/json"
        "Cookie" = $sessionCookie
    }
} catch {
    Write-Host "✗ Erreur connexion livreur: $($_.Exception.Message)" -ForegroundColor Red
    $cookieHeader = $headers
}

# 3. Test de connexion d'un commerçant (supposons qu'il existe déjà)
Write-Host "`n3. Test de connexion d'un commerçant..." -ForegroundColor Yellow

$commercantConnexion = @{
    email = "commercant@example.com"  # Remplacez par un email existant
    motDePasse = "password"           # Remplacez par le mot de passe correct
    role = "COMMERCANT"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/connexion" -Method POST -Body $commercantConnexion -Headers $headers -SessionVariable commercantSession
    $responseData = $response.Content | ConvertFrom-Json
    Write-Host "✓ Connexion commerçant réussie: $($responseData.message)" -ForegroundColor Green
    
    $commercantCookie = $commercantSession.Cookies.GetCookies("$baseUrl")
    $commercantHeader = @{
        "Content-Type" = "application/json"
        "Cookie" = $commercantCookie
    }
} catch {
    Write-Host "✗ Erreur connexion commerçant: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Note: Assurez-vous qu'un commerçant existe avec les identifiants fournis" -ForegroundColor Yellow
    $commercantHeader = $headers
}

# 4. Test de création d'une demande de livraison
Write-Host "`n4. Test de création d'une demande de livraison..." -ForegroundColor Yellow

$demandeData = @{
    adresseCollecte = "123 Rue du Commerce, 75001 Paris"
    adresseLivraison = "456 Avenue de la Livraison, 75002 Paris"
    nomDestinataire = "Martin Durand"
    telephoneDestinataire = "+33987654321"
    descriptionColis = "Colis fragile - Produits électroniques"
    poidsEstime = 2.5
    valeurDeclaree = 150.00
    fraisLivraison = 15.00
    priorite = "NORMALE"
    notesCommercant = "Livraison urgente avant 18h"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/demandes-livraison/creer" -Method POST -Body $demandeData -Headers $commercantHeader
    Write-Host "✓ Demande de livraison créée: $($response.message)" -ForegroundColor Green
    $demandeId = $response.data.id
} catch {
    Write-Host "✗ Erreur création demande: $($_.Exception.Message)" -ForegroundColor Red
    $demandeId = 1  # ID par défaut pour les tests suivants
}

# 5. Test de récupération des livreurs disponibles
Write-Host "`n5. Test de récupération des livreurs disponibles..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/livreurs/disponibles" -Method GET -Headers $headers
    Write-Host "✓ Livreurs disponibles récupérés: $($response.message)" -ForegroundColor Green
    if ($response.data.Count -gt 0) {
        $livreurId = $response.data[0].idLivreur
        Write-Host "Premier livreur disponible: $livreurId" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ Erreur récupération livreurs: $($_.Exception.Message)" -ForegroundColor Red
    $livreurId = "LIV001"  # ID par défaut
}

# 6. Test d'assignation d'un livreur à une demande
Write-Host "`n6. Test d'assignation d'un livreur..." -ForegroundColor Yellow

$assignationData = @{
    demandeId = $demandeId
    livreurId = $livreurId
    notes = "Livreur assigné automatiquement"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/demandes-livraison/assigner" -Method POST -Body $assignationData -Headers $commercantHeader
    Write-Host "✓ Livreur assigné: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur assignation livreur: $($_.Exception.Message)" -ForegroundColor Red
}

# 7. Test de récupération des demandes en attente
Write-Host "`n7. Test de récupération des demandes en attente..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/demandes-livraison/en-attente" -Method GET -Headers $headers
    Write-Host "✓ Demandes en attente récupérées: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur récupération demandes: $($_.Exception.Message)" -ForegroundColor Red
}

# 8. Test de récupération des demandes du livreur
Write-Host "`n8. Test de récupération des demandes du livreur..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/demandes-livraison/mes-demandes" -Method GET -Headers $cookieHeader
    Write-Host "✓ Demandes du livreur récupérées: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur récupération demandes livreur: $($_.Exception.Message)" -ForegroundColor Red
}

# 9. Test d'acceptation d'une demande par le livreur
Write-Host "`n9. Test d'acceptation d'une demande par le livreur..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/demandes-livraison/$demandeId/accepter" -Method POST -Headers $cookieHeader
    Write-Host "✓ Demande acceptée par le livreur: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur acceptation demande: $($_.Exception.Message)" -ForegroundColor Red
}

# 10. Test de modification du statut d'une demande
Write-Host "`n10. Test de modification du statut d'une demande..." -ForegroundColor Yellow

$statutData = @{
    demandeId = $demandeId
    nouveauStatut = "EN_COLLECTE"
    notes = "Livreur en route pour la collecte"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/demandes-livraison/statut" -Method PUT -Body $statutData -Headers $cookieHeader
    Write-Host "✓ Statut modifié: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur modification statut: $($_.Exception.Message)" -ForegroundColor Red
}

# 11. Test de modification du statut du livreur
Write-Host "`n11. Test de modification du statut du livreur..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/livreurs/statut?nouveauStatut=OCCUPE" -Method PUT -Headers $cookieHeader
    Write-Host "✓ Statut livreur modifié: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur modification statut livreur: $($_.Exception.Message)" -ForegroundColor Red
}

# 12. Test de récupération du profil du livreur
Write-Host "`n12. Test de récupération du profil du livreur..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/livreurs/profil" -Method GET -Headers $cookieHeader
    Write-Host "✓ Profil livreur récupéré: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "✗ Erreur récupération profil: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Tests terminés ===" -ForegroundColor Green
Write-Host "Note: Certains tests peuvent échouer si les données de test ne sont pas présentes." -ForegroundColor Yellow
Write-Host "Assurez-vous d'avoir au moins un commerçant enregistré pour tester toutes les fonctionnalités." -ForegroundColor Yellow
