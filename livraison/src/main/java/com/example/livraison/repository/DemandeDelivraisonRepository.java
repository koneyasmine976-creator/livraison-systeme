package com.example.livraison.repository;

import com.example.livraison.entity.DemandeDelivraison;
import com.example.livraison.entity.Commercant;
import com.example.livraison.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeDelivraisonRepository extends JpaRepository<DemandeDelivraison, Long> {
    
    Optional<DemandeDelivraison> findByNumeroDemande(String numeroDemande);
    
    List<DemandeDelivraison> findByCommercantOrderByDateCreationDesc(Commercant commercant);
    
    List<DemandeDelivraison> findByLivreurOrderByDateCreationDesc(Livreur livreur);
    
    @Query("SELECT d FROM DemandeDelivraison d WHERE d.statut = :statut ORDER BY d.dateCreation DESC")
    List<DemandeDelivraison> findByStatut(@Param("statut") DemandeDelivraison.StatutDemandeDelivraison statut);
    
    @Query("SELECT d FROM DemandeDelivraison d WHERE d.statut = 'EN_ATTENTE' ORDER BY d.priorite DESC, d.dateCreation ASC")
    List<DemandeDelivraison> findDemandesEnAttente();
    
    @Query("SELECT d FROM DemandeDelivraison d WHERE d.livreur = :livreur AND d.statut IN ('ASSIGNEE', 'ACCEPTEE', 'EN_COLLECTE', 'COLLECTEE', 'EN_LIVRAISON') ORDER BY d.dateCreation DESC")
    List<DemandeDelivraison> findDemandesActivesParLivreur(@Param("livreur") Livreur livreur);
    
    @Query("SELECT d FROM DemandeDelivraison d WHERE d.commercant = :commercant AND d.statut = :statut ORDER BY d.dateCreation DESC")
    List<DemandeDelivraison> findByCommercantAndStatut(@Param("commercant") Commercant commercant, @Param("statut") DemandeDelivraison.StatutDemandeDelivraison statut);
    
    @Query("SELECT COUNT(d) FROM DemandeDelivraison d WHERE d.commercant = :commercant")
    long countByCommercant(@Param("commercant") Commercant commercant);
    
    @Query("SELECT COUNT(d) FROM DemandeDelivraison d WHERE d.livreur = :livreur AND d.statut = 'LIVREE'")
    long countLivraisonsTermineesParLivreur(@Param("livreur") Livreur livreur);
    
    @Query("SELECT d FROM DemandeDelivraison d WHERE d.dateLivraisonPrevue BETWEEN :debut AND :fin")
    List<DemandeDelivraison> findByDateLivraisonPrevueBetween(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
}
