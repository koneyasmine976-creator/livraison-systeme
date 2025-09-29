package com.example.livraison.service;

import com.example.livraison.dto.CommandeResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.LigneCommande;
import com.example.livraison.exception.CommandeNotFoundException;
import com.example.livraison.repository.CommandeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    
    private final CommandeRepository commandeRepository;
    private final CommandeService commandeService;
    
    public byte[] genererReceiptPDF(Long commandeId, String userEmail, String userRole) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new CommandeNotFoundException("Commande non trouvée"));
        
        // Vérifier les permissions
        if ("CLIENT".equals(userRole)) {
            if (!commande.getClient().getEmail().equals(userEmail)) {
                throw new RuntimeException("Accès non autorisé à cette commande");
            }
        }
        
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            
            // En-tête
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            
            Paragraph title = new Paragraph("RÉCÉPISSÉ DE LIVRAISON", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            Paragraph subtitle = new Paragraph("Système de Gestion des Livraisons", normalFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            
            document.add(new Paragraph("\n"));
            
            // Informations de la commande
            document.add(new Paragraph("INFORMATIONS DE LA COMMANDE", headerFont));
            document.add(new Paragraph("Numéro de commande: " + commande.getNumeroCommande(), normalFont));
            document.add(new Paragraph("Date de commande: " + 
                    commande.getDateCommande().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
            document.add(new Paragraph("Statut: " + commande.getStatut().getDescription(), normalFont));
            
            if (commande.getDateLivraison() != null) {
                document.add(new Paragraph("Date de livraison: " + 
                        commande.getDateLivraison().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
            }
            
            document.add(new Paragraph("\n"));
            
            // Informations du client
            document.add(new Paragraph("INFORMATIONS DU CLIENT", headerFont));
            document.add(new Paragraph("Nom: " + commande.getClient().getPrenom() + " " + commande.getClient().getNom(), normalFont));
            document.add(new Paragraph("Email: " + commande.getClient().getEmail(), normalFont));
            document.add(new Paragraph("Téléphone: " + commande.getClient().getTelephone(), normalFont));
            
            document.add(new Paragraph("\n"));
            
            // Adresse de livraison
            if (commande.getAdresseLivraison() != null) {
                document.add(new Paragraph("ADRESSE DE LIVRAISON", headerFont));
                
                document.add(new Paragraph(commande.getAdresseLivraison().getRue(), normalFont));
                if (commande.getAdresseLivraison().getComplement() != null) {
                    document.add(new Paragraph(commande.getAdresseLivraison().getComplement(), normalFont));
                }
                document.add(new Paragraph(commande.getAdresseLivraison().getCodePostal() + " " + 
                        commande.getAdresseLivraison().getVille(), normalFont));
                document.add(new Paragraph(commande.getAdresseLivraison().getPays(), normalFont));
                
                if (commande.getAdresseLivraison().getTelephone() != null) {
                    document.add(new Paragraph("Tél: " + commande.getAdresseLivraison().getTelephone(), normalFont));
                }
                
                document.add(new Paragraph("\n"));
            }
            
            // Détails des produits
            document.add(new Paragraph("DÉTAILS DE LA COMMANDE", headerFont));
            
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            
            // En-têtes du tableau
            String[] headers = {"Produit", "Quantité", "Prix unitaire", "Sous-total"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            
            // Données du tableau
            for (LigneCommande ligne : commande.getLignesCommande()) {
                table.addCell(new PdfPCell(new Phrase(ligne.getProduit().getNom(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ligne.getQuantite()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f €", ligne.getPrixUnitaire()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f €", ligne.getSousTotal()), normalFont)));
            }
            
            document.add(table);
            
            document.add(new Paragraph("\n"));
            
            // Total
            Paragraph fraisLivraison = new Paragraph("Frais de livraison: " + String.format("%.2f €", commande.getFraisLivraison()), normalFont);
            fraisLivraison.setAlignment(Element.ALIGN_RIGHT);
            document.add(fraisLivraison);
            
            Paragraph total = new Paragraph("TOTAL: " + String.format("%.2f €", commande.getMontantTotal()), headerFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            
            if (commande.getNotes() != null && !commande.getNotes().trim().isEmpty()) {
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("NOTES:", headerFont));
                document.add(new Paragraph(commande.getNotes(), normalFont));
            }
            
            document.close();
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }
}
