package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.InscriptionClientRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.entity.Client;
import com.example.livraison.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientService clientService;
    
    @PostMapping("/inscription")
    public ResponseEntity<ApiResponse<UserInfo>> inscrireClient(@Valid @RequestBody InscriptionClientRequest request) {
        Client client = clientService.inscrireClient(request);
        UserInfo userInfo = clientService.convertirEnUserInfo(client);
        
        String message = "Inscription réussie pour " + userInfo.getPrenom() + " " + userInfo.getNom() + 
                        " (ID: " + userInfo.getId() + ")";
        return ResponseEntity.ok(ApiResponse.success(message, userInfo));
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<UserInfo>> getClientByEmail(@PathVariable String email) {
        var clientOpt = clientService.trouverParEmail(email);
        if (clientOpt.isPresent()) {
            UserInfo userInfo = clientService.convertirEnUserInfo(clientOpt.get());
            return ResponseEntity.ok(ApiResponse.success("Client trouvé: " + userInfo.getPrenom() + " " + userInfo.getNom(), userInfo));
        } else {
            return ResponseEntity.ok(ApiResponse.error("Aucun client trouvé avec l'email '" + email + "'"));
        }
    }
}
