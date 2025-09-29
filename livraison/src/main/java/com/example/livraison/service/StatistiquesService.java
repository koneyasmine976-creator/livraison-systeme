package com.example.livraison.service;

import com.example.livraison.dto.StatistiquesResponse;
import com.example.livraison.entity.*;
import com.example.livraison.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatistiquesService {
    
    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final CommercantRepository commerçantRepository;
    private final ProduitRepository produitRepository;
    private final MessageSupportRepository messageSupportRepository;
    
    public StatistiquesResponse getStatistiquesGenerales() {
        StatistiquesResponse response = new StatistiquesResponse();
        response.setDateGeneration(LocalDateTime.now());
        response.setPeriode("Général");
        
        // Statistiques générales
        response.setTotalCommandes(commandeRepository.count());
        response.setTotalClients(clientRepository.count());
        response.setTotalCommercants(commerçantRepository.count());
        response.setTotalProduits(produitRepository.count());
        
        // Chiffre d'affaires total
        BigDecimal chiffreAffairesTotal = commandeRepository.findAll().stream()
                .map(Commande::getMontantTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setChiffreAffairesTotal(chiffreAffairesTotal);
        
        // Commandes par statut
        Map<String, Long> commandesParStatut = commandeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        commande -> commande.getStatut().name(),
                        Collectors.counting()
                ));
        response.setCommandesParStatut(commandesParStatut);
        
        // Top commandes (les 10 plus récentes)
        List<StatistiquesResponse.CommandeStatistique> topCommandes = commandeRepository.findAll().stream()
                .sorted((c1, c2) -> c2.getDateCommande().compareTo(c1.getDateCommande()))
                .limit(10)
                .map(this::convertirEnCommandeStatistique)
                .collect(Collectors.toList());
        response.setTopCommandes(topCommandes);
        
        // Top produits (par chiffre d'affaires)
        List<StatistiquesResponse.ProduitStatistique> topProduits = produitRepository.findAll().stream()
                .map(this::convertirEnProduitStatistique)
                .sorted((p1, p2) -> p2.getChiffreAffaires().compareTo(p1.getChiffreAffaires()))
                .limit(10)
                .collect(Collectors.toList());
        response.setTopProduits(topProduits);
        
        // Ventes par catégorie
        Map<String, Long> ventesParCategorie = produitRepository.findAll().stream()
                .filter(p -> p.getCategorie() != null)
                .collect(Collectors.groupingBy(
                        Produit::getCategorie,
                        Collectors.counting()
                ));
        response.setVentesParCategorie(ventesParCategorie);
        
        // Top clients (par nombre de commandes)
        List<StatistiquesResponse.ClientStatistique> topClients = clientRepository.findAll().stream()
                .map(this::convertirEnClientStatistique)
                .sorted((c1, c2) -> Long.compare(c2.getNombreCommandes(), c1.getNombreCommandes()))
                .limit(10)
                .collect(Collectors.toList());
        response.setTopClients(topClients);
        
        // Clients par pays
        Map<String, Long> clientsParPays = clientRepository.findAll().stream()
                .filter(c -> c.getPaysResidence() != null)
                .collect(Collectors.groupingBy(
                        Client::getPaysResidence,
                        Collectors.counting()
                ));
        response.setClientsParPays(clientsParPays);
        
        // Top commerçants
        List<StatistiquesResponse.CommercantStatistique> topCommercants = commerçantRepository.findAll().stream()
                .map(this::convertirEnCommercantStatistique)
                .sorted((c1, c2) -> Long.compare(c2.getNombreCommandes(), c1.getNombreCommandes()))
                .limit(10)
                .collect(Collectors.toList());
        response.setTopCommercants(topCommercants);
        
        return response;
    }
    
    public StatistiquesResponse getStatistiquesParPeriode(LocalDateTime debut, LocalDateTime fin) {
        StatistiquesResponse response = new StatistiquesResponse();
        response.setDateGeneration(LocalDateTime.now());
        response.setPeriode("Du " + debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                          " au " + fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Commandes dans la période
        List<Commande> commandesPeriode = commandeRepository.findByDateCommandeBetweenOrderByDateCommandeDesc(debut, fin);
        
        response.setTotalCommandes((long) commandesPeriode.size());
        response.setTotalClients(clientRepository.count());
        response.setTotalCommercants(commerçantRepository.count());
        response.setTotalProduits(produitRepository.count());
        
        // Chiffre d'affaires de la période
        BigDecimal chiffreAffairesPeriode = commandesPeriode.stream()
                .map(Commande::getMontantTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setChiffreAffairesTotal(chiffreAffairesPeriode);
        
        // Commandes par statut dans la période
        Map<String, Long> commandesParStatut = commandesPeriode.stream()
                .collect(Collectors.groupingBy(
                        commande -> commande.getStatut().name(),
                        Collectors.counting()
                ));
        response.setCommandesParStatut(commandesParStatut);
        
        // Top commandes de la période
        List<StatistiquesResponse.CommandeStatistique> topCommandes = commandesPeriode.stream()
                .sorted((c1, c2) -> c2.getDateCommande().compareTo(c1.getDateCommande()))
                .limit(10)
                .map(this::convertirEnCommandeStatistique)
                .collect(Collectors.toList());
        response.setTopCommandes(topCommandes);
        
        // Autres statistiques (simplifiées pour la période)
        response.setTopProduits(new ArrayList<>());
        response.setVentesParCategorie(new HashMap<>());
        response.setTopClients(new ArrayList<>());
        response.setClientsParPays(new HashMap<>());
        response.setTopCommercants(new ArrayList<>());
        
        return response;
    }
    
    private StatistiquesResponse.CommandeStatistique convertirEnCommandeStatistique(Commande commande) {
        StatistiquesResponse.CommandeStatistique stat = new StatistiquesResponse.CommandeStatistique();
        stat.setId(commande.getId());
        stat.setNumeroCommande(commande.getNumeroCommande());
        stat.setClientNom(commande.getClient().getPrenom() + " " + commande.getClient().getNom());
        stat.setMontantTotal(commande.getMontantTotal());
        stat.setStatut(commande.getStatut().name());
        stat.setDateCommande(commande.getDateCommande());
        return stat;
    }
    
    private StatistiquesResponse.ProduitStatistique convertirEnProduitStatistique(Produit produit) {
        StatistiquesResponse.ProduitStatistique stat = new StatistiquesResponse.ProduitStatistique();
        stat.setId(produit.getId());
        stat.setNom(produit.getNom());
        stat.setCategorie(produit.getCategorie());
        stat.setStockRestant(produit.getStock());
        
        // Calculer les ventes (simplifié)
        long quantiteVendue = produit.getCommerçant() != null ? 
                commandeRepository.findByCommercantOrderByDateCommandeDesc(produit.getCommerçant()).stream()
                        .flatMap(c -> c.getLignesCommande().stream())
                        .filter(l -> l.getProduit().getId().equals(produit.getId()))
                        .mapToLong(LigneCommande::getQuantite)
                        .sum() : 0;
        
        stat.setQuantiteVendue(quantiteVendue);
        stat.setChiffreAffaires(produit.getPrix().multiply(BigDecimal.valueOf(quantiteVendue)));
        
        return stat;
    }
    
    private StatistiquesResponse.ClientStatistique convertirEnClientStatistique(Client client) {
        StatistiquesResponse.ClientStatistique stat = new StatistiquesResponse.ClientStatistique();
        stat.setId(Long.parseLong(client.getIdClient().replace("CLI", "")));
        stat.setNom(client.getPrenom() + " " + client.getNom());
        stat.setEmail(client.getEmail());
        
        List<Commande> commandesClient = commandeRepository.findByClientOrderByDateCommandeDesc(client);
        stat.setNombreCommandes((long) commandesClient.size());
        
        BigDecimal montantTotal = commandesClient.stream()
                .map(Commande::getMontantTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stat.setMontantTotal(montantTotal);
        
        stat.setDerniereCommande(commandesClient.isEmpty() ? null : 
                commandesClient.get(0).getDateCommande());
        
        return stat;
    }
    
    private StatistiquesResponse.CommercantStatistique convertirEnCommercantStatistique(Commercant commerçant) {
        StatistiquesResponse.CommercantStatistique stat = new StatistiquesResponse.CommercantStatistique();
        stat.setId(Long.parseLong(commerçant.getIdCommercant().replace("COM", "")));
        stat.setNom(commerçant.getPrenom() + " " + commerçant.getNom());
        stat.setNomBoutique(commerçant.getNomBoutique());
        stat.setEmail(commerçant.getEmail());
        
        List<Commande> commandesCommercant = commandeRepository.findByCommercantOrderByDateCommandeDesc(commerçant);
        stat.setNombreCommandes((long) commandesCommercant.size());
        
        BigDecimal chiffreAffaires = commandesCommercant.stream()
                .map(Commande::getMontantTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stat.setChiffreAffaires(chiffreAffaires);
        
        long nombreProduits = produitRepository.findByCommerçantAndActifTrueOrderByNomAsc(commerçant).size();
        stat.setNombreProduits(nombreProduits);
        
        return stat;
    }
}
