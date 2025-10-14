package com.example.livraison.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignerLivreurRequest {
    
    @NotNull(message = "L'ID de la demande est obligatoire")
    private Long demandeId;
    
    @NotBlank(message = "L'ID du livreur est obligatoire")
    private String livreurId;
    
    private String notes;
}
