package com.example.livraison.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreerDemandeDelivraisonRequest {
    
    private Long commandeId;
    
    @NotBlank(message = "L'adresse de collecte est obligatoire")
    private String adresseCollecte;
    
    @NotBlank(message = "L'adresse de livraison est obligatoire")
    private String adresseLivraison;
    
    @NotBlank(message = "Le nom du destinataire est obligatoire")
    private String nomDestinataire;
    
    @NotBlank(message = "Le téléphone du destinataire est obligatoire")
    private String telephoneDestinataire;
    
    private String descriptionColis;
    
    @Positive(message = "Le poids doit être positif")
    private Double poidsEstime;
    
    @Positive(message = "La valeur déclarée doit être positive")
    private BigDecimal valeurDeclaree;
    
    @NotNull(message = "Les frais de livraison sont obligatoires")
    @Positive(message = "Les frais de livraison doivent être positifs")
    private BigDecimal fraisLivraison;
    
    private String priorite = "NORMALE";
    
    private LocalDateTime dateLivraisonPrevue;
    
    private String notesCommercant;
}
