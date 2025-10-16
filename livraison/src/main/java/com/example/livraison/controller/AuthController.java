package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.ConnexionRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour la gestion de l'authentification et des sessions")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/connexion")
    @Operation(
        summary = "Connexion utilisateur",
        description = "Authentifie un utilisateur (CLIENT, COMMERCANT ou LIVREUR) et crée une session. Retourne les informations de l'utilisateur connecté."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Connexion réussie",
            content = @Content(schema = @Schema(implementation = UserInfo.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Identifiants invalides"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Compte bloqué ou désactivé"
        )
    })
    public ResponseEntity<ApiResponse<UserInfo>> connecter(@Valid @RequestBody ConnexionRequest request, 
                                                           HttpServletRequest httpRequest) {
        UserInfo userInfo = authService.connecter(request);
        
        // Créer une session
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("user", userInfo);
        session.setAttribute("userId", userInfo.getId());
        session.setAttribute("role", userInfo.getRole());
        
        String message = "Connexion réussie pour " + userInfo.getPrenom() + " " + userInfo.getNom() + 
                        " (" + userInfo.getRole() + ")";
        return ResponseEntity.ok(ApiResponse.success(message, userInfo));
    }
    
    @PostMapping("/deconnexion")
    @Operation(
        summary = "Déconnexion utilisateur",
        description = "Invalide la session active de l'utilisateur et le déconnecte du système."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Déconnexion réussie"
        )
    })
    public ResponseEntity<ApiResponse<String>> deconnecter(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(ApiResponse.success("Déconnexion réussie"));
    }
    
    @GetMapping("/session")
    @Operation(
        summary = "Informations de session",
        description = "Récupère les informations de l'utilisateur actuellement connecté via sa session active."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Informations de session récupérées",
            content = @Content(schema = @Schema(implementation = UserInfo.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Aucune session active"
        )
    })
    public ResponseEntity<ApiResponse<UserInfo>> getSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            UserInfo userInfo = (UserInfo) session.getAttribute("user");
            return ResponseEntity.ok(ApiResponse.success("Session active pour " + userInfo.getPrenom() + " " + userInfo.getNom(), userInfo));
        } else {
            return ResponseEntity.ok(ApiResponse.error("Aucune session active"));
        }
    }
}
