package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponse {
    
    private Long id;
    private String numeroCommande;
    private String clientNom;
    private String clientEmail;
    private AdresseResponse adresseLivraison;
    private LocalDateTime dateCommande;
    private LocalDateTime dateLivraison;
    private String statut;
    private String statutDescription;
    private BigDecimal montantTotal;
    private BigDecimal fraisLivraison;
    private String notes;
    private List<LigneCommandeResponse> lignesCommande;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdresseResponse {
        private Long id;
        private String rue;
        private String ville;
        private String codePostal;
        private String pays;
        private String complement;
        private String telephone;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneCommandeResponse {
        private Long id;
        private String produitNom;
        private Integer quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal sousTotal;
    }
}
