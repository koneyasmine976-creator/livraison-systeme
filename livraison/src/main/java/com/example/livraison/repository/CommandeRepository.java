package com.example.livraison.repository;

import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Commercant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    
    List<Commande> findByClientOrderByDateCommandeDesc(Client client);
    
    @Query("SELECT c FROM Commande c JOIN c.lignesCommande lc JOIN lc.produit p WHERE p.commerçant = :commerçant ORDER BY c.dateCommande DESC")
    List<Commande> findByCommercantOrderByDateCommandeDesc(@Param("commerçant") Commercant commerçant);
    
    List<Commande> findByStatutOrderByDateCommandeDesc(Commande.StatutCommande statut);
    
    @Query("SELECT c FROM Commande c WHERE c.dateCommande BETWEEN :debut AND :fin ORDER BY c.dateCommande DESC")
    List<Commande> findByDateCommandeBetweenOrderByDateCommandeDesc(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
    
    Commande findByNumeroCommande(String numeroCommande);
}
