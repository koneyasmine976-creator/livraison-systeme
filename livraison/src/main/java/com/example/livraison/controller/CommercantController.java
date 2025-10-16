package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.InscriptionCommercantRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.entity.Commercant;
import com.example.livraison.service.CommercantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commercants")
@RequiredArgsConstructor
@Tag(name = "Commerçants", description = "Endpoints pour la gestion des commerçants et de leurs boutiques")
public class CommercantController {
    
    private final CommercantService commerçantService;
    
    @PostMapping("/inscription")
    @Operation(
        summary = "Inscription d'un commerçant",
        description = "Crée un nouveau compte commerçant avec les informations de la boutique. L'email doit être unique."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Inscription réussie",
            content = @Content(schema = @Schema(implementation = UserInfo.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Email déjà utilisé ou données invalides"
        )
    })
    public ResponseEntity<ApiResponse<UserInfo>> inscrireCommercant(@Valid @RequestBody InscriptionCommercantRequest request) {
        Commercant commerçant = commerçantService.inscrireCommercant(request);
        UserInfo userInfo = commerçantService.convertirEnUserInfo(commerçant);
        
        String message = "Inscription réussie pour " + userInfo.getPrenom() + " " + userInfo.getNom() + 
                        " (Boutique: " + userInfo.getNomBoutique() + ", ID: " + userInfo.getId() + ")";
        return ResponseEntity.ok(ApiResponse.success(message, userInfo));
    }
    
    @GetMapping("/{email}")
    @Operation(
        summary = "Rechercher un commerçant par email",
        description = "Récupère les informations d'un commerçant en utilisant son adresse email."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Commerçant trouvé",
            content = @Content(schema = @Schema(implementation = UserInfo.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Aucun commerçant trouvé avec cet email"
        )
    })
    public ResponseEntity<ApiResponse<UserInfo>> getCommercantByEmail(@PathVariable String email) {
        var commerçantOpt = commerçantService.trouverParEmail(email);
        if (commerçantOpt.isPresent()) {
            UserInfo userInfo = commerçantService.convertirEnUserInfo(commerçantOpt.get());
            return ResponseEntity.ok(ApiResponse.success("Commerçant trouvé: " + userInfo.getPrenom() + " " + userInfo.getNom() + 
                        " (Boutique: " + userInfo.getNomBoutique() + ")", userInfo));
        } else {
            return ResponseEntity.ok(ApiResponse.error("Aucun commerçant trouvé avec l'email '" + email + "'"));
        }
    }
}
