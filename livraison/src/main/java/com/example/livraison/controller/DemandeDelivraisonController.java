package com.example.livraison.controller;

import com.example.livraison.dto.*;
import com.example.livraison.entity.DemandeDelivraison;
import com.example.livraison.service.DemandeDelivraisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes-livraison")
@RequiredArgsConstructor
@Tag(name = "Demandes de Livraison", description = "Endpoints pour la gestion des demandes de livraison")
public class DemandeDelivraisonController {
    
    private final DemandeDelivraisonService demandeService;
    
    @PostMapping("/creer")
    @Operation(
        summary = "Créer une demande de livraison",
        description = "Permet à un commerçant de créer une nouvelle demande de livraison. Nécessite d'être connecté en tant que COMMERCANT.",
        security = @SecurityRequirement(name = "session")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Demande créée avec succès",
            content = @Content(schema = @Schema(implementation = DemandeDelivraison.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Session non valide"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Accès réservé aux commerçants"
        )
    })
    public ResponseEntity<ApiResponse<DemandeDelivraison>> creerDemande(
            @Valid @RequestBody CreerDemandeDelivraisonRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String role = (String) session.getAttribute("role");
        if (!"ROLE_COMMERCANT".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Accès réservé aux commerçants"));
        }
        
        String commercantId = (String) session.getAttribute("userId");
        if (commercantId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Utilisateur non identifié"));
        }
        
        try {
            DemandeDelivraison demande = demandeService.creerDemande(request, commercantId);
            return ResponseEntity.ok(ApiResponse.success(
                "Demande de livraison créée avec succès. Numéro: " + demande.getNumeroDemande(), 
                demande
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/assigner")
    @Operation(
        summary = "Assigner un livreur à une demande",
        description = "Permet au commerçant d'assigner un livreur à une demande de livraison. Le livreur passe automatiquement en statut OCCUPE.",
        security = @SecurityRequirement(name = "session")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Livreur assigné avec succès"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Accès réservé aux commerçants"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Demande ou livreur non trouvé"
        )
    })
    public ResponseEntity<ApiResponse<DemandeDelivraison>> assignerLivreur(
            @Valid @RequestBody AssignerLivreurRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String role = (String) session.getAttribute("role");
        if (!"ROLE_COMMERCANT".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Accès réservé aux commerçants"));
        }
        
        try {
            DemandeDelivraison demande = demandeService.assignerLivreur(
                request.getDemandeId(), 
                request.getLivreurId(), 
                request.getNotes()
            );
            return ResponseEntity.ok(ApiResponse.success(
                "Livreur assigné avec succès à la demande " + demande.getNumeroDemande(), 
                demande
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/statut")
    public ResponseEntity<ApiResponse<DemandeDelivraison>> modifierStatut(
            @Valid @RequestBody ModifierStatutDemandeRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String utilisateurId = (String) session.getAttribute("userId");
        if (utilisateurId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Utilisateur non identifié"));
        }
        
        try {
            DemandeDelivraison demande = demandeService.modifierStatut(
                request.getDemandeId(), 
                request.getNouveauStatut(), 
                request.getNotes(),
                utilisateurId
            );
            return ResponseEntity.ok(ApiResponse.success(
                "Statut modifié avec succès pour la demande " + demande.getNumeroDemande(), 
                demande
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/mes-demandes")
    @Operation(
        summary = "Mes demandes de livraison",
        description = "Récupère les demandes selon le rôle : toutes les demandes pour un commerçant, ou demandes assignées pour un livreur.",
        security = @SecurityRequirement(name = "session")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Liste des demandes récupérée"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Session non valide"
        )
    })
    public ResponseEntity<ApiResponse<List<DemandeDelivraisonResponse>>> obtenirMesDemandes(
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String role = (String) session.getAttribute("role");
        String utilisateurId = (String) session.getAttribute("userId");
        
        try {
            List<DemandeDelivraisonResponse> demandes;
            
            if ("ROLE_COMMERCANT".equals(role)) {
                demandes = demandeService.obtenirDemandesParCommercant(utilisateurId);
            } else if ("ROLE_LIVREUR".equals(role)) {
                demandes = demandeService.obtenirDemandesParLivreur(utilisateurId);
            } else {
                return ResponseEntity.status(403).body(ApiResponse.error("Rôle non autorisé"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(
                "Demandes trouvées (" + demandes.size() + " résultat(s))", 
                demandes
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/mes-livraisons-actives")
    public ResponseEntity<ApiResponse<List<DemandeDelivraisonResponse>>> obtenirMesLivraisonsActives(
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String role = (String) session.getAttribute("role");
        if (!"ROLE_LIVREUR".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Accès réservé aux livreurs"));
        }
        
        String livreurId = (String) session.getAttribute("userId");
        
        try {
            List<DemandeDelivraisonResponse> demandes = demandeService.obtenirDemandesActivesParLivreur(livreurId);
            return ResponseEntity.ok(ApiResponse.success(
                "Livraisons actives (" + demandes.size() + " résultat(s))", 
                demandes
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/en-attente")
    public ResponseEntity<ApiResponse<List<DemandeDelivraisonResponse>>> obtenirDemandesEnAttente() {
        try {
            List<DemandeDelivraisonResponse> demandes = demandeService.obtenirDemandesEnAttente();
            return ResponseEntity.ok(ApiResponse.success(
                "Demandes en attente (" + demandes.size() + " résultat(s))", 
                demandes
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DemandeDelivraisonResponse>> obtenirDemandeParId(@PathVariable Long id) {
        return demandeService.obtenirDemandeParId(id)
                .map(demande -> ResponseEntity.ok(ApiResponse.success("Détails de la demande", demande)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{id}/accepter")
    public ResponseEntity<ApiResponse<DemandeDelivraison>> accepterDemande(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String role = (String) session.getAttribute("role");
        if (!"ROLE_LIVREUR".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Accès réservé aux livreurs"));
        }
        
        String livreurId = (String) session.getAttribute("userId");
        
        try {
            DemandeDelivraison demande = demandeService.modifierStatut(
                id, 
                "ACCEPTEE", 
                "Demande acceptée par le livreur",
                livreurId
            );
            return ResponseEntity.ok(ApiResponse.success(
                "Demande acceptée avec succès", demande
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/refuser")
    public ResponseEntity<ApiResponse<DemandeDelivraison>> refuserDemande(
            @PathVariable Long id,
            @RequestParam(required = false) String motif,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String role = (String) session.getAttribute("role");
        if (!"ROLE_LIVREUR".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Accès réservé aux livreurs"));
        }
        
        String livreurId = (String) session.getAttribute("userId");
        
        try {
            String notes = "Demande refusée par le livreur";
            if (motif != null && !motif.trim().isEmpty()) {
                notes += ". Motif: " + motif;
            }
            
            DemandeDelivraison demande = demandeService.modifierStatut(
                id, 
                "REFUSEE", 
                notes,
                livreurId
            );
            return ResponseEntity.ok(ApiResponse.success(
                "Demande refusée", demande
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
