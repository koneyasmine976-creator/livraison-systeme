package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private LocalDate dateInscription;
    
    // Champs spécifiques au client
    private String adresse;
    private String paysResidence;
    private Boolean statutCompte;
    private Boolean premiereConnexion;
    
    // Champs spécifiques au commerçant
    private String nomBoutique;
    private String adresseBoutique;
    private String logoBoutique;
}
