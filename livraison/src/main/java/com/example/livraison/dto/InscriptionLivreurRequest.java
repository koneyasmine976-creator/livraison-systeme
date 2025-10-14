package com.example.livraison.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InscriptionLivreurRequest {
    
    @NotBlank(message = "L'ID du livreur est obligatoire")
    private String idLivreur;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Format de téléphone invalide")
    private String telephone;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
    
    private String numeroPermis;
    
    @NotBlank(message = "Le type de véhicule est obligatoire")
    private String typeVehicule;
    
    private String plaqueVehicule;
}
