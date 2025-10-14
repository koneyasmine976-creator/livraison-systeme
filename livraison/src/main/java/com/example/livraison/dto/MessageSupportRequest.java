package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSupportRequest {
    
    @NotBlank(message = "L'objet est obligatoire")
    @Size(max = 200, message = "L'objet ne peut pas dépasser 200 caractères")
    private String objet;
    
    @NotBlank(message = "Le contenu est obligatoire")
    @Size(max = 2000, message = "Le contenu ne peut pas dépasser 2000 caractères")
    private String contenu;
    
    private String priorite = "NORMALE";
}
