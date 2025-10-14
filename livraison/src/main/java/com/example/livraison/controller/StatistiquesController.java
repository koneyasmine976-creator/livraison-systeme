package com.example.livraison.controller;

import com.example.livraison.dto.ApiResponse;
import com.example.livraison.dto.StatistiquesResponse;
import com.example.livraison.service.StatistiquesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
public class StatistiquesController {
    
    private final StatistiquesService statistiquesService;
    
    @GetMapping("/generales")
    public ResponseEntity<ApiResponse<StatistiquesResponse>> getStatistiquesGenerales(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"ADMIN".equals(userRole) && !"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les administrateurs et commerçants peuvent consulter les statistiques"));
        }
        
        StatistiquesResponse statistiques = statistiquesService.getStatistiquesGenerales();
        
        String message = "Statistiques générales récupérées avec succès";
        return ResponseEntity.ok(ApiResponse.success(message, statistiques));
    }
    
    @GetMapping("/periode")
    public ResponseEntity<ApiResponse<StatistiquesResponse>> getStatistiquesParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"ADMIN".equals(userRole) && !"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les administrateurs et commerçants peuvent consulter les statistiques"));
        }
        
        if (debut.isAfter(fin)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("La date de début doit être antérieure à la date de fin"));
        }
        
        StatistiquesResponse statistiques = statistiquesService.getStatistiquesParPeriode(debut, fin);
        
        String message = "Statistiques de la période récupérées avec succès";
        return ResponseEntity.ok(ApiResponse.success(message, statistiques));
    }
    
    @GetMapping("/export/pdf")
    public ResponseEntity<ApiResponse<String>> exporterStatistiquesPDF(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"ADMIN".equals(userRole) && !"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les administrateurs et commerçants peuvent exporter les statistiques"));
        }
        
        // Pour l'instant, on retourne un message indiquant que l'export PDF sera implémenté
        String message = "Export PDF des statistiques (fonctionnalité à implémenter)";
        return ResponseEntity.ok(ApiResponse.success(message));
    }
    
    @GetMapping("/export/excel")
    public ResponseEntity<ApiResponse<String>> exporterStatistiquesExcel(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Session non trouvée. Veuillez vous connecter."));
        }
        
        String userRole = (String) session.getAttribute("role");
        if (!"ADMIN".equals(userRole) && !"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Seuls les administrateurs et commerçants peuvent exporter les statistiques"));
        }
        
        // Pour l'instant, on retourne un message indiquant que l'export Excel sera implémenté
        String message = "Export Excel des statistiques (fonctionnalité à implémenter)";
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
