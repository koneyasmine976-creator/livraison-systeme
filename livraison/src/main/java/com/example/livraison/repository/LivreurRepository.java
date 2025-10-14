package com.example.livraison.repository;

import com.example.livraison.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, String> {
    
    Optional<Livreur> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByTelephone(String telephone);
    
    @Query("SELECT l FROM Livreur l WHERE l.statut = 'DISPONIBLE' AND l.actif = true")
    List<Livreur> findLivreursDisponibles();
    
    @Query("SELECT l FROM Livreur l WHERE l.statut = :statut AND l.actif = true")
    List<Livreur> findByStatut(@Param("statut") Livreur.StatutLivreur statut);
    
    List<Livreur> findByActifTrue();
    
    @Query("SELECT COUNT(l) FROM Livreur l WHERE l.actif = true")
    long countLivreursActifs();
}
