package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionCommercantRequest {
    
    @NotBlank(message = "L'ID commerçant est obligatoire")
    private String idCommercant;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;
    
    @NotBlank(message = "Le nom de la boutique est obligatoire")
    private String nomBoutique;
    
    @NotBlank(message = "L'adresse de la boutique est obligatoire")
    private String adresseBoutique;
    
    private String logoBoutique;
}
