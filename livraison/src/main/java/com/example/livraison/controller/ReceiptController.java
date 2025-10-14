package com.example.livraison.controller;

import com.example.livraison.service.ReceiptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    
    private final ReceiptService receiptService;
    
    @GetMapping("/{commandeId}/download")
    public ResponseEntity<byte[]> telechargerReceipt(
            @PathVariable Long commandeId,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(401).build();
        }
        
        String userEmail = (String) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");
        
        // Vérifier que l'utilisateur a le droit de télécharger ce reçu
        if (!"CLIENT".equals(userRole) && !"COMMERCANT".equals(userRole)) {
            return ResponseEntity.status(403).build();
        }
        
        try {
            byte[] pdfBytes = receiptService.genererReceiptPDF(commandeId, userEmail, userRole);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "receipt_" + commandeId + "_" + timestamp + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
