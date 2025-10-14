package com.example.livraison.service;

import com.example.livraison.dto.CommandeResponse;
import com.example.livraison.dto.CreerCommandeRequest;
import com.example.livraison.dto.ModifierStatutCommandeRequest;
import com.example.livraison.entity.*;
import com.example.livraison.exception.CommandeNotFoundException;
import com.example.livraison.exception.InsufficientStockException;
import com.example.livraison.exception.UnauthorizedAccessException;
import com.example.livraison.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {
    
    private final CommandeRepository commandeRepository;
    private final ProduitService produitService;
    private final AdresseService adresseService;
    private final ClientService clientService;
    private final CommercantService commerçantService;
    
    @Transactional
    public CommandeResponse creerCommande(String clientEmail, CreerCommandeRequest request) {
        // Récupérer le client
        Client client = clientService.trouverParEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        // Vérifier l'adresse de livraison
        Adresse adresseLivraison = adresseService.trouverParIdEtClient(request.getAdresseLivraisonId(), client)
                .orElseThrow(() -> new RuntimeException("Adresse de livraison non trouvée"));
        
        // Créer la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setAdresseLivraison(adresseLivraison);
        commande.setNotes(request.getNotes());
        
        // Calculer le montant total
        BigDecimal montantTotal = BigDecimal.ZERO;
        
        for (CreerCommandeRequest.LigneCommandeRequest ligneRequest : request.getLignesCommande()) {
            Produit produit = produitService.trouverParId(ligneRequest.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
            
            // Vérifier le stock
            if (produit.getStock() < ligneRequest.getQuantite()) {
                throw new InsufficientStockException("Stock insuffisant pour le produit: " + produit.getNom());
            }
            
            // Créer la ligne de commande
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setCommande(commande);
            ligneCommande.setProduit(produit);
            ligneCommande.setQuantite(ligneRequest.getQuantite());
            ligneCommande.setPrixUnitaire(produit.getPrix());
            
            // Mettre à jour le stock
            produit.setStock(produit.getStock() - ligneRequest.getQuantite());
            produitService.sauvegarderProduit(produit);
            
            montantTotal = montantTotal.add(ligneCommande.getSousTotal());
        }
        
        commande.setMontantTotal(montantTotal);
        
        // Sauvegarder la commande
        Commande commandeSauvegardee = commandeRepository.save(commande);
        
        return convertirEnCommandeResponse(commandeSauvegardee);
    }
    
    public List<CommandeResponse> getCommandesClient(String clientEmail) {
        Client client = clientService.trouverParEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        List<Commande> commandes = commandeRepository.findByClientOrderByDateCommandeDesc(client);
        return commandes.stream()
                .map(this::convertirEnCommandeResponse)
                .collect(Collectors.toList());
    }
    
    public List<CommandeResponse> getCommandesCommercant(String commerçantEmail) {
        Commercant commerçant = commerçantService.trouverParEmail(commerçantEmail)
                .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
        
        List<Commande> commandes = commandeRepository.findByCommercantOrderByDateCommandeDesc(commerçant);
        return commandes.stream()
                .map(this::convertirEnCommandeResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CommandeResponse modifierStatutCommande(Long commandeId, String userEmail, String userRole, ModifierStatutCommandeRequest request) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new CommandeNotFoundException("Commande non trouvée"));
        
        // Vérifier les permissions
        if ("CLIENT".equals(userRole)) {
            if (!commande.getClient().getEmail().equals(userEmail)) {
                throw new UnauthorizedAccessException("Accès non autorisé à cette commande");
            }
            // Les clients ne peuvent que voir leurs commandes, pas modifier le statut
            throw new UnauthorizedAccessException("Les clients ne peuvent pas modifier le statut des commandes");
        }
        
        // Changer le statut
        try {
            Commande.StatutCommande nouveauStatut = Commande.StatutCommande.valueOf(request.getStatut());
            commande.setStatut(nouveauStatut);
            
            if (request.getNotes() != null) {
                commande.setNotes(commande.getNotes() + "\n" + request.getNotes());
            }
            
            Commande commandeModifiee = commandeRepository.save(commande);
            return convertirEnCommandeResponse(commandeModifiee);
            
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide: " + request.getStatut());
        }
    }
    
    public CommandeResponse getCommandeParId(Long commandeId, String userEmail, String userRole) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new CommandeNotFoundException("Commande non trouvée"));
        
        // Vérifier les permissions
        if ("CLIENT".equals(userRole)) {
            if (!commande.getClient().getEmail().equals(userEmail)) {
                throw new UnauthorizedAccessException("Accès non autorisé à cette commande");
            }
        }
        
        return convertirEnCommandeResponse(commande);
    }
    
    private CommandeResponse convertirEnCommandeResponse(Commande commande) {
        CommandeResponse response = new CommandeResponse();
        response.setId(commande.getId());
        response.setNumeroCommande(commande.getNumeroCommande());
        response.setClientNom(commande.getClient().getPrenom() + " " + commande.getClient().getNom());
        response.setClientEmail(commande.getClient().getEmail());
        
        // Adresse de livraison
        if (commande.getAdresseLivraison() != null) {
            CommandeResponse.AdresseResponse adresseResponse = new CommandeResponse.AdresseResponse();
            adresseResponse.setId(commande.getAdresseLivraison().getId());
            adresseResponse.setRue(commande.getAdresseLivraison().getRue());
            adresseResponse.setVille(commande.getAdresseLivraison().getVille());
            adresseResponse.setCodePostal(commande.getAdresseLivraison().getCodePostal());
            adresseResponse.setPays(commande.getAdresseLivraison().getPays());
            adresseResponse.setComplement(commande.getAdresseLivraison().getComplement());
            adresseResponse.setTelephone(commande.getAdresseLivraison().getTelephone());
            response.setAdresseLivraison(adresseResponse);
        }
        
        response.setDateCommande(commande.getDateCommande());
        response.setDateLivraison(commande.getDateLivraison());
        response.setStatut(commande.getStatut().name());
        response.setStatutDescription(commande.getStatut().getDescription());
        response.setMontantTotal(commande.getMontantTotal());
        response.setFraisLivraison(commande.getFraisLivraison());
        response.setNotes(commande.getNotes());
        
        // Lignes de commande
        if (commande.getLignesCommande() != null) {
            List<CommandeResponse.LigneCommandeResponse> lignesResponse = commande.getLignesCommande().stream()
                    .map(ligne -> {
                        CommandeResponse.LigneCommandeResponse ligneResponse = new CommandeResponse.LigneCommandeResponse();
                        ligneResponse.setId(ligne.getId());
                        ligneResponse.setProduitNom(ligne.getProduit().getNom());
                        ligneResponse.setQuantite(ligne.getQuantite());
                        ligneResponse.setPrixUnitaire(ligne.getPrixUnitaire());
                        ligneResponse.setSousTotal(ligne.getSousTotal());
                        return ligneResponse;
                    })
                    .collect(Collectors.toList());
            response.setLignesCommande(lignesResponse);
        }
        
        return response;
    }
}
