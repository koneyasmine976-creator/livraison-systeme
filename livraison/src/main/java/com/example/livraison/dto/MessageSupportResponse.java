package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSupportResponse {
    
    private Long id;
    private String objet;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private String statut;
    private String statutDescription;
    private String reponse;
    private LocalDateTime dateReponse;
    private String priorite;
    private String expediteurNom;
    private String expediteurEmail;
    private String expediteurRole;
}
