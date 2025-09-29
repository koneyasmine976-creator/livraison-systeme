package com.example.livraison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_commande", unique = true)
    private String numeroCommande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse_livraison")
    private Adresse adresseLivraison;
    
    @Column(name = "date_commande")
    private LocalDateTime dateCommande;
    
    @Column(name = "date_livraison")
    private LocalDateTime dateLivraison;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutCommande statut;
    
    @Column(name = "montant_total", precision = 10, scale = 2)
    private BigDecimal montantTotal;
    
    @Column(name = "frais_livraison", precision = 10, scale = 2)
    private BigDecimal fraisLivraison = BigDecimal.ZERO;
    
    @Column(name = "notes")
    private String notes;
    
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneCommande> lignesCommande;
    
    @PrePersist
    protected void onCreate() {
        if (dateCommande == null) {
            dateCommande = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutCommande.EN_ATTENTE;
        }
        if (numeroCommande == null) {
            numeroCommande = "CMD" + System.currentTimeMillis();
        }
    }
    
    public enum StatutCommande {
        EN_ATTENTE("En attente de traitement"),
        EN_COURS("En cours de préparation"),
        PRETE("Prête pour livraison"),
        EN_LIVRAISON("En cours de livraison"),
        LIVREE("Livrée"),
        ANNULEE("Annulée");
        
        private final String description;
        
        StatutCommande(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
