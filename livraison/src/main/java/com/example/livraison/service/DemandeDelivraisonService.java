package com.example.livraison.service;

import com.example.livraison.dto.CreerDemandeDelivraisonRequest;
import com.example.livraison.dto.DemandeDelivraisonResponse;
import com.example.livraison.entity.*;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.CommercantRepository;
import com.example.livraison.repository.DemandeDelivraisonRepository;
import com.example.livraison.repository.LivreurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandeDelivraisonService {
    
    private final DemandeDelivraisonRepository demandeRepository;
    private final CommercantRepository commercantRepository;
    private final LivreurRepository livreurRepository;
    private final CommandeRepository commandeRepository;
    
    public DemandeDelivraison creerDemande(CreerDemandeDelivraisonRequest request, String commercantId) {
        Commercant commercant = commercantRepository.findById(commercantId)
                .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
        
        DemandeDelivraison demande = new DemandeDelivraison();
        demande.setCommercant(commercant);
        
        // Si une commande est spécifiée, l'associer
        if (request.getCommandeId() != null) {
            Commande commande = commandeRepository.findById(request.getCommandeId())
                    .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
            demande.setCommande(commande);
        }
        
        demande.setAdresseCollecte(request.getAdresseCollecte());
        demande.setAdresseLivraison(request.getAdresseLivraison());
        demande.setNomDestinataire(request.getNomDestinataire());
        demande.setTelephoneDestinataire(request.getTelephoneDestinataire());
        demande.setDescriptionColis(request.getDescriptionColis());
        demande.setPoidsEstime(request.getPoidsEstime());
        demande.setValeurDeclaree(request.getValeurDeclaree());
        demande.setFraisLivraison(request.getFraisLivraison());
        demande.setDateLivraisonPrevue(request.getDateLivraisonPrevue());
        demande.setNotesCommercant(request.getNotesCommercant());
        
        // Définir la priorité
        try {
            demande.setPriorite(DemandeDelivraison.PrioriteDelivraison.valueOf(request.getPriorite()));
        } catch (IllegalArgumentException e) {
            demande.setPriorite(DemandeDelivraison.PrioriteDelivraison.NORMALE);
        }
        
        return demandeRepository.save(demande);
    }
    
    public DemandeDelivraison assignerLivreur(Long demandeId, String livreurId, String notes) {
        DemandeDelivraison demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande de livraison non trouvée"));
        
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        if (!livreur.getActif()) {
            throw new RuntimeException("Le livreur n'est pas actif");
        }
        
        demande.setLivreur(livreur);
        demande.setStatut(DemandeDelivraison.StatutDemandeDelivraison.ASSIGNEE);
        demande.setDateAssignation(LocalDateTime.now());
        
        if (notes != null && !notes.trim().isEmpty()) {
            demande.setNotesCommercant(demande.getNotesCommercant() + "\n" + notes);
        }
        
        // Mettre le livreur en statut occupé
        livreur.setStatut(Livreur.StatutLivreur.OCCUPE);
        livreurRepository.save(livreur);
        
        return demandeRepository.save(demande);
    }
    
    public DemandeDelivraison modifierStatut(Long demandeId, String nouveauStatut, String notes, String utilisateurId) {
        DemandeDelivraison demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande de livraison non trouvée"));
        
        DemandeDelivraison.StatutDemandeDelivraison statut;
        try {
            statut = DemandeDelivraison.StatutDemandeDelivraison.valueOf(nouveauStatut);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide: " + nouveauStatut);
        }
        
        // Vérifier les transitions de statut autorisées
        if (!estTransitionAutorisee(demande.getStatut(), statut)) {
            throw new RuntimeException("Transition de statut non autorisée de " + 
                    demande.getStatut() + " vers " + statut);
        }
        
        demande.setStatut(statut);
        
        // Mettre à jour les dates selon le statut
        switch (statut) {
            case EN_ATTENTE:
                // Aucune action spécifique
                break;
            case ASSIGNEE:
                // Aucune action spécifique (géré dans assignerLivreur)
                break;
            case ACCEPTEE:
                // Aucune action spécifique
                break;
            case EN_COLLECTE:
                // Aucune action spécifique
                break;
            case COLLECTEE:
                demande.setDateCollecte(LocalDateTime.now());
                break;
            case EN_LIVRAISON:
                // Aucune action spécifique
                break;
            case LIVREE:
                demande.setDateLivraisonReelle(LocalDateTime.now());
                // Libérer le livreur
                if (demande.getLivreur() != null) {
                    demande.getLivreur().setStatut(Livreur.StatutLivreur.DISPONIBLE);
                    livreurRepository.save(demande.getLivreur());
                }
                break;
            case ANNULEE:
            case REFUSEE:
                // Libérer le livreur si assigné
                if (demande.getLivreur() != null) {
                    demande.getLivreur().setStatut(Livreur.StatutLivreur.DISPONIBLE);
                    livreurRepository.save(demande.getLivreur());
                }
                break;
        }
        
        // Ajouter les notes
        if (notes != null && !notes.trim().isEmpty()) {
            // Déterminer si c'est un livreur ou un commerçant qui ajoute la note
            if (demande.getLivreur() != null && demande.getLivreur().getIdLivreur().equals(utilisateurId)) {
                demande.setNotesLivreur(demande.getNotesLivreur() != null ? 
                        demande.getNotesLivreur() + "\n" + notes : notes);
            } else {
                demande.setNotesCommercant(demande.getNotesCommercant() != null ? 
                        demande.getNotesCommercant() + "\n" + notes : notes);
            }
        }
        
        return demandeRepository.save(demande);
    }
    
    private boolean estTransitionAutorisee(DemandeDelivraison.StatutDemandeDelivraison ancien, 
                                          DemandeDelivraison.StatutDemandeDelivraison nouveau) {
        // Logique de validation des transitions de statut
        switch (ancien) {
            case EN_ATTENTE:
                return nouveau == DemandeDelivraison.StatutDemandeDelivraison.ASSIGNEE ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.ANNULEE;
            case ASSIGNEE:
                return nouveau == DemandeDelivraison.StatutDemandeDelivraison.ACCEPTEE ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.REFUSEE ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.ANNULEE;
            case ACCEPTEE:
                return nouveau == DemandeDelivraison.StatutDemandeDelivraison.EN_COLLECTE ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.ANNULEE;
            case EN_COLLECTE:
                return nouveau == DemandeDelivraison.StatutDemandeDelivraison.COLLECTEE ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.ANNULEE;
            case COLLECTEE:
                return nouveau == DemandeDelivraison.StatutDemandeDelivraison.EN_LIVRAISON ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.ANNULEE;
            case EN_LIVRAISON:
                return nouveau == DemandeDelivraison.StatutDemandeDelivraison.LIVREE ||
                       nouveau == DemandeDelivraison.StatutDemandeDelivraison.ANNULEE;
            default:
                return false;
        }
    }
    
    @Transactional(readOnly = true)
    public List<DemandeDelivraisonResponse> obtenirDemandesParCommercant(String commercantId) {
        Commercant commercant = commercantRepository.findById(commercantId)
                .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
        
        return demandeRepository.findByCommercantOrderByDateCreationDesc(commercant)
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DemandeDelivraisonResponse> obtenirDemandesParLivreur(String livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        return demandeRepository.findByLivreurOrderByDateCreationDesc(livreur)
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DemandeDelivraisonResponse> obtenirDemandesEnAttente() {
        return demandeRepository.findDemandesEnAttente()
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DemandeDelivraisonResponse> obtenirDemandesActivesParLivreur(String livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        return demandeRepository.findDemandesActivesParLivreur(livreur)
                .stream()
                .map(this::convertirEnResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<DemandeDelivraisonResponse> obtenirDemandeParId(Long id) {
        return demandeRepository.findById(id)
                .map(this::convertirEnResponse);
    }
    
    private DemandeDelivraisonResponse convertirEnResponse(DemandeDelivraison demande) {
        DemandeDelivraisonResponse response = new DemandeDelivraisonResponse();
        response.setId(demande.getId());
        response.setNumeroDemande(demande.getNumeroDemande());
        response.setCommercantNom(demande.getCommercant().getNom() + " " + demande.getCommercant().getPrenom());
        response.setCommercantTelephone(demande.getCommercant().getTelephone());
        
        if (demande.getLivreur() != null) {
            response.setLivreurNom(demande.getLivreur().getNom() + " " + demande.getLivreur().getPrenom());
            response.setLivreurTelephone(demande.getLivreur().getTelephone());
        }
        
        response.setAdresseCollecte(demande.getAdresseCollecte());
        response.setAdresseLivraison(demande.getAdresseLivraison());
        response.setNomDestinataire(demande.getNomDestinataire());
        response.setTelephoneDestinataire(demande.getTelephoneDestinataire());
        response.setDescriptionColis(demande.getDescriptionColis());
        response.setPoidsEstime(demande.getPoidsEstime());
        response.setValeurDeclaree(demande.getValeurDeclaree());
        response.setFraisLivraison(demande.getFraisLivraison());
        response.setStatut(demande.getStatut().name());
        response.setPriorite(demande.getPriorite().name());
        response.setDateCreation(demande.getDateCreation());
        response.setDateAssignation(demande.getDateAssignation());
        response.setDateCollecte(demande.getDateCollecte());
        response.setDateLivraisonPrevue(demande.getDateLivraisonPrevue());
        response.setDateLivraisonReelle(demande.getDateLivraisonReelle());
        response.setNotesCommercant(demande.getNotesCommercant());
        response.setNotesLivreur(demande.getNotesLivreur());
        
        return response;
    }
}
