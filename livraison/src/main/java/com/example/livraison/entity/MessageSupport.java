package com.example.livraison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages_support")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSupport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "objet", nullable = false)
    private String objet;
    
    @Column(name = "contenu", nullable = false, columnDefinition = "TEXT")
    private String contenu;
    
    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutMessage statut;
    
    @Column(name = "reponse")
    private String reponse;
    
    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_commercant")
    private Commercant commerçant;
    
    @Column(name = "priorite")
    private String priorite = "NORMALE";
    
    @PrePersist
    protected void onCreate() {
        if (dateEnvoi == null) {
            dateEnvoi = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutMessage.EN_ATTENTE;
        }
    }
    
    public enum StatutMessage {
        EN_ATTENTE("En attente"),
        EN_COURS("En cours de traitement"),
        RESOLU("Résolu"),
        FERME("Fermé");
        
        private final String description;
        
        StatutMessage(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
