package com.example.livraison.service;

import com.example.livraison.dto.ProduitRequest;
import com.example.livraison.entity.Commercant;
import com.example.livraison.entity.Produit;
import com.example.livraison.exception.ProduitNotFoundException;
import com.example.livraison.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProduitService {
    
    private final ProduitRepository produitRepository;
    private final CommercantService commerçantService;
    
    public Produit creerProduit(String commerçantEmail, ProduitRequest request) {
        Commercant commerçant = commerçantService.trouverParEmail(commerçantEmail)
                .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
        
        Produit produit = new Produit();
        produit.setNom(request.getNom());
        produit.setDescription(request.getDescription());
        produit.setPrix(request.getPrix());
        produit.setStock(request.getStock() != null ? request.getStock() : 0);
        produit.setCategorie(request.getCategorie());
        produit.setImageUrl(request.getImageUrl());
        produit.setCommerçant(commerçant);
        produit.setActif(true);
        
        return produitRepository.save(produit);
    }
    
    public List<Produit> getProduitsCommercant(String commerçantEmail) {
        Commercant commerçant = commerçantService.trouverParEmail(commerçantEmail)
                .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
        
        return produitRepository.findByCommerçantAndActifTrueOrderByNomAsc(commerçant);
    }
    
    public List<Produit> getTousProduits() {
        return produitRepository.findByActifTrueOrderByNomAsc();
    }
    
    public Optional<Produit> trouverParId(Long id) {
        return produitRepository.findById(id);
    }
    
    public Produit sauvegarderProduit(Produit produit) {
        return produitRepository.save(produit);
    }
    
    public void supprimerProduit(Long id, String commerçantEmail) {
        Commercant commerçant = commerçantService.trouverParEmail(commerçantEmail)
                .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
        
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ProduitNotFoundException("Produit non trouvé"));
        
        if (!produit.getCommerçant().getEmail().equals(commerçantEmail)) {
            throw new RuntimeException("Accès non autorisé à ce produit");
        }
        
        produit.setActif(false);
        produitRepository.save(produit);
    }
}
