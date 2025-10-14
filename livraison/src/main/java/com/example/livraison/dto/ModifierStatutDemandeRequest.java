package com.example.livraison.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifierStatutDemandeRequest {
    
    @NotNull(message = "L'ID de la demande est obligatoire")
    private Long demandeId;
    
    @NotBlank(message = "Le nouveau statut est obligatoire")
    private String nouveauStatut;
    
    private String notes;
}
