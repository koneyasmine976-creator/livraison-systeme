package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseRequest {
    
    @NotBlank(message = "La rue est obligatoire")
    private String rue;
    
    @NotBlank(message = "La ville est obligatoire")
    private String ville;
    
    @NotBlank(message = "Le code postal est obligatoire")
    private String codePostal;
    
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;
    
    private String complement;
    
    private String telephone;
    
    private Boolean principale = false;
}
