package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreerCommandeRequest {
    
    @NotNull(message = "L'ID de l'adresse de livraison est obligatoire")
    private Long adresseLivraisonId;
    
    @NotEmpty(message = "La commande doit contenir au moins un produit")
    @Valid
    private List<LigneCommandeRequest> lignesCommande;
    
    private String notes;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneCommandeRequest {
        
        @NotNull(message = "L'ID du produit est obligatoire")
        private Long produitId;
        
        @NotNull(message = "La quantité est obligatoire")
        private Integer quantite;
    }
}
