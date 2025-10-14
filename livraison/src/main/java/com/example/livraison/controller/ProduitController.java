package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.ProduitRequest;
import com.example.livraison.entity.Produit;
import com.example.livraison.service.ProduitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {
    
    private final ProduitService produitService;
    
    @PostMapping("/creer")
    public ResponseEntity<ApiResponse<Produit>> creerProduit(
            @Valid @RequestBody ProduitRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les commerçants peuvent créer des produits"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        Produit produit = produitService.creerProduit(userEmail, request);
        
        String message = "Produit créé avec succès: " + produit.getNom();
        return ResponseEntity.ok(ApiResponse.success(message, produit));
    }
    
    @GetMapping("/mes-produits")
    public ResponseEntity<ApiResponse<List<Produit>>> getMesProduits(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les commerçants peuvent accéder à leurs produits"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        List<Produit> produits = produitService.getProduitsCommercant(userEmail);
        
        String message = "Liste des produits récupérée (" + produits.size() + " produit(s))";
        return ResponseEntity.ok(ApiResponse.success(message, produits));
    }
    
    @GetMapping("/catalogue")
    public ResponseEntity<ApiResponse<List<Produit>>> getCatalogue() {
        List<Produit> produits = produitService.getTousProduits();
        
        String message = "Catalogue récupéré (" + produits.size() + " produit(s))";
        return ResponseEntity.ok(ApiResponse.success(message, produits));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> supprimerProduit(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les commerçants peuvent supprimer leurs produits"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        produitService.supprimerProduit(id, userEmail);
        
        String message = "Produit supprimé avec succès";
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
