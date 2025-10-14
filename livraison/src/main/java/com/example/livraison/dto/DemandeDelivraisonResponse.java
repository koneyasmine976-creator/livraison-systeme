package com.example.livraison.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DemandeDelivraisonResponse {
    
    private Long id;
    private String numeroDemande;
    private String commercantNom;
    private String commercantTelephone;
    private String livreurNom;
    private String livreurTelephone;
    private String adresseCollecte;
    private String adresseLivraison;
    private String nomDestinataire;
    private String telephoneDestinataire;
    private String descriptionColis;
    private Double poidsEstime;
    private BigDecimal valeurDeclaree;
    private BigDecimal fraisLivraison;
    private String statut;
    private String priorite;
    private LocalDateTime dateCreation;
    private LocalDateTime dateAssignation;
    private LocalDateTime dateCollecte;
    private LocalDateTime dateLivraisonPrevue;
    private LocalDateTime dateLivraisonReelle;
    private String notesCommercant;
    private String notesLivreur;
}
