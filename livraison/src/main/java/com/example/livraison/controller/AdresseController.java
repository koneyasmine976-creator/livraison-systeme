package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.AdresseRequest;
import com.example.livraison.entity.Adresse;
import com.example.livraison.service.AdresseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AdresseController {
    
    private final AdresseService adresseService;
    
    @PostMapping("/creer")
    public ResponseEntity<ApiResponse<Adresse>> creerAdresse(
            @Valid @RequestBody AdresseRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"CLIENT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les clients peuvent créer des adresses"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        Adresse adresse = adresseService.creerAdresse(userEmail, request);
        
        String message = "Adresse créée avec succès: " + adresse.getRue() + ", " + adresse.getVille();
        return ResponseEntity.ok(ApiResponse.success(message, adresse));
    }
    
    @GetMapping("/mes-adresses")
    public ResponseEntity<ApiResponse<List<Adresse>>> getMesAdresses(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"CLIENT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les clients peuvent accéder à leurs adresses"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        List<Adresse> adresses = adresseService.getAdressesClient(userEmail);
        
        String message = "Liste des adresses récupérée (" + adresses.size() + " adresse(s))";
        return ResponseEntity.ok(ApiResponse.success(message, adresses));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> supprimerAdresse(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"CLIENT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les clients peuvent supprimer leurs adresses"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        adresseService.supprimerAdresse(id, userEmail);
        
        String message = "Adresse supprimée avec succès";
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
