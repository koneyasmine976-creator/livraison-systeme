package com.example.livraison.repository;

import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commercant;
import com.example.livraison.entity.MessageSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageSupportRepository extends JpaRepository<MessageSupport, Long> {
    
    List<MessageSupport> findByClientOrderByDateEnvoiDesc(Client client);
    
    List<MessageSupport> findByCommerçantOrderByDateEnvoiDesc(Commercant commerçant);
    
    List<MessageSupport> findByStatutOrderByDateEnvoiDesc(MessageSupport.StatutMessage statut);
    
    @Query("SELECT m FROM MessageSupport m WHERE m.dateEnvoi BETWEEN :debut AND :fin ORDER BY m.dateEnvoi DESC")
    List<MessageSupport> findByDateEnvoiBetweenOrderByDateEnvoiDesc(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT m FROM MessageSupport m WHERE m.priorite = :priorite ORDER BY m.dateEnvoi DESC")
    List<MessageSupport> findByPrioriteOrderByDateEnvoiDesc(@Param("priorite") String priorite);
}
