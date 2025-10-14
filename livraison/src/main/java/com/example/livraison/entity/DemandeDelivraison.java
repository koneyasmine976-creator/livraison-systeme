package com.example.livraison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_delivraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeDelivraison {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_demande", unique = true)
    private String numeroDemande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_commercant", nullable = false)
    private Commercant commercant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_commande")
    private Commande commande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livreur")
    private Livreur livreur;
    
    @Column(name = "adresse_collecte", nullable = false)
    private String adresseCollecte;
    
    @Column(name = "adresse_livraison", nullable = false)
    private String adresseLivraison;
    
    @Column(name = "nom_destinataire", nullable = false)
    private String nomDestinataire;
    
    @Column(name = "telephone_destinataire", nullable = false)
    private String telephoneDestinataire;
    
    @Column(name = "description_colis")
    private String descriptionColis;
    
    @Column(name = "poids_estime")
    private Double poidsEstime;
    
    @Column(name = "valeur_declaree", precision = 10, scale = 2)
    private BigDecimal valeurDeclaree;
    
    @Column(name = "frais_livraison", precision = 10, scale = 2)
    private BigDecimal fraisLivraison;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutDemandeDelivraison statut;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priorite")
    private PrioriteDelivraison priorite = PrioriteDelivraison.NORMALE;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    
    @Column(name = "date_assignation")
    private LocalDateTime dateAssignation;
    
    @Column(name = "date_collecte")
    private LocalDateTime dateCollecte;
    
    @Column(name = "date_livraison_prevue")
    private LocalDateTime dateLivraisonPrevue;
    
    @Column(name = "date_livraison_reelle")
    private LocalDateTime dateLivraisonReelle;
    
    @Column(name = "notes_commercant")
    private String notesCommercant;
    
    @Column(name = "notes_livreur")
    private String notesLivreur;
    
    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutDemandeDelivraison.EN_ATTENTE;
        }
        if (numeroDemande == null) {
            numeroDemande = "DEL" + System.currentTimeMillis();
        }
    }
    
    public enum StatutDemandeDelivraison {
        EN_ATTENTE("En attente d'assignation"),
        ASSIGNEE("Assignée à un livreur"),
        ACCEPTEE("Acceptée par le livreur"),
        EN_COLLECTE("En cours de collecte"),
        COLLECTEE("Collectée"),
        EN_LIVRAISON("En cours de livraison"),
        LIVREE("Livrée"),
        ANNULEE("Annulée"),
        REFUSEE("Refusée par le livreur");
        
        private final String description;
        
        StatutDemandeDelivraison(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum PrioriteDelivraison {
        BASSE("Basse"),
        NORMALE("Normale"),
        HAUTE("Haute"),
        URGENTE("Urgente");
        
        private final String description;
        
        PrioriteDelivraison(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
