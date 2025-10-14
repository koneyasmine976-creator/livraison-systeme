package com.example.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifierStatutCommandeRequest {
    
    @NotNull(message = "Le statut est obligatoire")
    private String statut;
    
    private String notes;
}
