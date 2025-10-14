package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.ConnexionRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/connexion")
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
    public ResponseEntity<ApiResponse<String>> deconnecter(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(ApiResponse.success("Déconnexion réussie"));
    }
    
    @GetMapping("/session")
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
