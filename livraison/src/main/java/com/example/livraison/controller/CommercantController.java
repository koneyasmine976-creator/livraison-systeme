package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.InscriptionCommercantRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.entity.Commercant;
import com.example.livraison.service.CommercantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commercants")
@RequiredArgsConstructor
public class CommercantController {
    
    private final CommercantService commerçantService;
    
    @PostMapping("/inscription")
    public ResponseEntity<ApiResponse<UserInfo>> inscrireCommercant(@Valid @RequestBody InscriptionCommercantRequest request) {
        Commercant commerçant = commerçantService.inscrireCommercant(request);
        UserInfo userInfo = commerçantService.convertirEnUserInfo(commerçant);
        
        String message = "Inscription réussie pour " + userInfo.getPrenom() + " " + userInfo.getNom() + 
                        " (Boutique: " + userInfo.getNomBoutique() + ", ID: " + userInfo.getId() + ")";
        return ResponseEntity.ok(ApiResponse.success(message, userInfo));
    }
    
    @GetMapping("/{email}")
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
