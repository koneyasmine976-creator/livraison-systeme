package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.MessageSupportRequest;
import com.example.livraison.dto.MessageSupportResponse;
import com.example.livraison.service.MessageSupportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class MessageSupportController {
    
    private final MessageSupportService messageSupportService;
    
    @PostMapping("/envoyer")
    public ResponseEntity<ApiResponse<MessageSupportResponse>> envoyerMessage(
            @Valid @RequestBody MessageSupportRequest request,
            HttpServletRequest httpRequest) {
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userEmail = (String) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");
        
        if (!"CLIENT".equals(userRole) && !"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les clients et commerçants peuvent contacter le support"));
        }
        
        MessageSupportResponse message = messageSupportService.envoyerMessage(userEmail, userRole, request);
        
        String messageText = "Message envoyé au support avec succès (ID: " + message.getId() + ")";
        return ResponseEntity.ok(ApiResponse.success(messageText, message));
    }
    
    @GetMapping("/mes-messages")
    public ResponseEntity<ApiResponse<List<MessageSupportResponse>>> getMesMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userEmail = (String) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");
        
        List<MessageSupportResponse> messages = messageSupportService.getMessagesUtilisateur(userEmail, userRole);
        
        String messageText = "Liste des messages récupérée (" + messages.size() + " message(s))";
        return ResponseEntity.ok(ApiResponse.success(messageText, messages));
    }
    
    @GetMapping("/tous-messages")
    public ResponseEntity<ApiResponse<List<MessageSupportResponse>>> getTousMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les administrateurs peuvent voir tous les messages"));
        }
        
        List<MessageSupportResponse> messages = messageSupportService.getTousMessages();
        
        String messageText = "Liste de tous les messages récupérée (" + messages.size() + " message(s))";
        return ResponseEntity.ok(ApiResponse.success(messageText, messages));
    }
    
    @PostMapping("/{id}/repondre")
    public ResponseEntity<ApiResponse<MessageSupportResponse>> repondreMessage(
            @PathVariable Long id,
            @RequestBody String reponse,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les administrateurs peuvent répondre aux messages"));
        }
        
        MessageSupportResponse message = messageSupportService.repondreMessage(id, reponse);
        
        String messageText = "Réponse envoyée avec succès pour le message " + message.getId();
        return ResponseEntity.ok(ApiResponse.success(messageText, message));
    }
}
