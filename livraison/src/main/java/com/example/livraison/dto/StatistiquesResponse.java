package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesResponse {
    
    private LocalDateTime dateGeneration;
    private String periode;
    
    // Statistiques générales
    private Long totalCommandes;
    private Long totalClients;
    private Long totalCommercants;
    private Long totalProduits;
    private BigDecimal chiffreAffairesTotal;
    
    // Statistiques des commandes
    private Map<String, Long> commandesParStatut;
    private Map<String, BigDecimal> chiffreAffairesParMois;
    private List<CommandeStatistique> topCommandes;
    
    // Statistiques des produits
    private List<ProduitStatistique> topProduits;
    private Map<String, Long> ventesParCategorie;
    
    // Statistiques des clients
    private List<ClientStatistique> topClients;
    private Map<String, Long> clientsParPays;
    
    // Statistiques des commerçants
    private List<CommercantStatistique> topCommercants;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandeStatistique {
        private Long id;
        private String numeroCommande;
        private String clientNom;
        private BigDecimal montantTotal;
        private String statut;
        private LocalDateTime dateCommande;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitStatistique {
        private Long id;
        private String nom;
        private String categorie;
        private Long quantiteVendue;
        private BigDecimal chiffreAffaires;
        private Integer stockRestant;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientStatistique {
        private Long id;
        private String nom;
        private String email;
        private Long nombreCommandes;
        private BigDecimal montantTotal;
        private LocalDateTime derniereCommande;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommercantStatistique {
        private Long id;
        private String nom;
        private String nomBoutique;
        private String email;
        private Long nombreCommandes;
        private BigDecimal chiffreAffaires;
        private Long nombreProduits;
    }
}
