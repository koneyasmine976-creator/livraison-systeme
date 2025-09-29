package com.example.livraison.service;

import com.example.livraison.dto.AdresseRequest;
import com.example.livraison.entity.Adresse;
import com.example.livraison.entity.Client;
import com.example.livraison.repository.AdresseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdresseService {
    
    private final AdresseRepository adresseRepository;
    private final ClientService clientService;
    
    public Adresse creerAdresse(String clientEmail, AdresseRequest request) {
        Client client = clientService.trouverParEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        // Si c'est la première adresse ou si elle est marquée comme principale
        if (request.getPrincipale() || adresseRepository.findByClientOrderByPrincipaleDesc(client).isEmpty()) {
            // Désactiver les autres adresses principales
            adresseRepository.findByClientOrderByPrincipaleDesc(client).forEach(adresse -> {
                adresse.setPrincipale(false);
                adresseRepository.save(adresse);
            });
        }
        
        Adresse adresse = new Adresse();
        adresse.setRue(request.getRue());
        adresse.setVille(request.getVille());
        adresse.setCodePostal(request.getCodePostal());
        adresse.setPays(request.getPays());
        adresse.setComplement(request.getComplement());
        adresse.setTelephone(request.getTelephone());
        adresse.setClient(client);
        adresse.setPrincipale(request.getPrincipale());
        
        return adresseRepository.save(adresse);
    }
    
    public List<Adresse> getAdressesClient(String clientEmail) {
        Client client = clientService.trouverParEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        return adresseRepository.findByClientOrderByPrincipaleDesc(client);
    }
    
    public Optional<Adresse> trouverParIdEtClient(Long id, Client client) {
        return adresseRepository.findByClientAndId(client, id).stream().findFirst();
    }
    
    public void supprimerAdresse(Long id, String clientEmail) {
        Client client = clientService.trouverParEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        Adresse adresse = adresseRepository.findByClientAndId(client, id).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));
        
        adresseRepository.delete(adresse);
    }
}
