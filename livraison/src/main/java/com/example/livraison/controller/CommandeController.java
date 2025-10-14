package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.CommandeResponse;
import com.example.livraison.dto.CreerCommandeRequest;
import com.example.livraison.dto.ModifierStatutCommandeRequest;
import com.example.livraison.service.CommandeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {
    
    private final CommandeService commandeService;
    
    @PostMapping("/creer")
    public ResponseEntity<ApiResponse<CommandeResponse>> creerCommande(
            @Valid @RequestBody CreerCommandeRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"CLIENT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les clients peuvent créer des commandes"));
        }
        
        String userEmail = (String) session.getAttribute("user");
        CommandeResponse commande = commandeService.creerCommande(userEmail, request);
        
        String message = "Commande créée avec succès (N°: " + commande.getNumeroCommande() + ")";
        return ResponseEntity.ok(ApiResponse.success(message, commande));
    }
    
    @GetMapping("/mes-commandes")
    public ResponseEntity<ApiResponse<List<CommandeResponse>>> getMesCommandes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userEmail = (String) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");
        
        List<CommandeResponse> commandes;
        if ("CLIENT".equals(userRole)) {
            commandes = commandeService.getCommandesClient(userEmail);
        } else if ("COMMERCANT".equals(userRole)) {
            commandes = commandeService.getCommandesCommercant(userEmail);
        } else {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Rôle non autorisé"));
        }
        
        String message = "Liste des commandes récupérée (" + commandes.size() + " commande(s))";
        return ResponseEntity.ok(ApiResponse.success(message, commandes));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommandeResponse>> getCommandeParId(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userEmail = (String) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");
        
        CommandeResponse commande = commandeService.getCommandeParId(id, userEmail, userRole);
        
        String message = "Commande récupérée: " + commande.getNumeroCommande();
        return ResponseEntity.ok(ApiResponse.success(message, commande));
    }
    
    @PutMapping("/{id}/statut")
    public ResponseEntity<ApiResponse<CommandeResponse>> modifierStatutCommande(
            @PathVariable Long id,
            @Valid @RequestBody ModifierStatutCommandeRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userEmail = (String) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");
        
        CommandeResponse commande = commandeService.modifierStatutCommande(id, userEmail, userRole, request);
        
        String message = "Statut de la commande " + commande.getNumeroCommande() + 
                        " modifié vers: " + commande.getStatutDescription();
        return ResponseEntity.ok(ApiResponse.success(message, commande));
    }
}
