package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.InscriptionLivreurRequest;
import com.example.livraison.entity.Livreur;
import com.example.livraison.service.LivreurService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
public class LivreurController {
    
    private final LivreurService livreurService;
    
    @PostMapping("/inscription")
    public ResponseEntity<ApiResponse<Livreur>> inscrireLivreur(@Valid @RequestBody InscriptionLivreurRequest request) {
        try {
            Livreur livreur = livreurService.inscrireLivreur(request);
            return ResponseEntity.ok(ApiResponse.success(
                "Inscription réussie pour le livreur " + livreur.getPrenom() + " " + livreur.getNom(), 
                livreur
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/profil")
    public ResponseEntity<ApiResponse<Livreur>> obtenirProfil(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String livreurId = (String) session.getAttribute("userId");
        if (livreurId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Utilisateur non identifié"));
        }
        
        return livreurService.trouverParId(livreurId)
                .map(livreur -> ResponseEntity.ok(ApiResponse.success("Profil du livreur", livreur)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/statut")
    public ResponseEntity<ApiResponse<Livreur>> modifierStatut(
            @RequestParam String nouveauStatut,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Session non valide"));
        }
        
        String livreurId = (String) session.getAttribute("userId");
        if (livreurId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Utilisateur non identifié"));
        }
        
        try {
            Livreur.StatutLivreur statut = Livreur.StatutLivreur.valueOf(nouveauStatut);
            Livreur livreur = livreurService.modifierStatut(livreurId, statut);
            return ResponseEntity.ok(ApiResponse.success(
                "Statut modifié avec succès", livreur
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Statut invalide"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<Livreur>>> obtenirLivreursDisponibles() {
        List<Livreur> livreurs = livreurService.obtenirLivreursDisponibles();
        return ResponseEntity.ok(ApiResponse.success(
            "Liste des livreurs disponibles (" + livreurs.size() + " trouvé(s))", 
            livreurs
        ));
    }
    
    @GetMapping("/tous")
    public ResponseEntity<ApiResponse<List<Livreur>>> obtenirTousLesLivreurs() {
        List<Livreur> livreurs = livreurService.obtenirTousLesLivreursActifs();
        return ResponseEntity.ok(ApiResponse.success(
            "Liste de tous les livreurs actifs (" + livreurs.size() + " trouvé(s))", 
            livreurs
        ));
    }
    
    @PutMapping("/{livreurId}/activer")
    public ResponseEntity<ApiResponse<Livreur>> activerLivreur(@PathVariable String livreurId) {
        try {
            Livreur livreur = livreurService.activerDesactiverLivreur(livreurId, true);
            return ResponseEntity.ok(ApiResponse.success("Livreur activé avec succès", livreur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{livreurId}/desactiver")
    public ResponseEntity<ApiResponse<Livreur>> desactiverLivreur(@PathVariable String livreurId) {
        try {
            Livreur livreur = livreurService.activerDesactiverLivreur(livreurId, false);
            return ResponseEntity.ok(ApiResponse.success("Livreur désactivé avec succès", livreur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/statistiques")
    public ResponseEntity<ApiResponse<Long>> obtenirStatistiques() {
        long nombreLivreursActifs = livreurService.compterLivreursActifs();
        return ResponseEntity.ok(ApiResponse.success(
            "Nombre de livreurs actifs", nombreLivreursActifs
        ));
    }
}
