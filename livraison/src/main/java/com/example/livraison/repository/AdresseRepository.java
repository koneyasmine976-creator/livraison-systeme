package com.example.livraison.repository;

import com.example.livraison.entity.Adresse;
import com.example.livraison.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    
    List<Adresse> findByClientOrderByPrincipaleDesc(Client client);
    
    Optional<Adresse> findByClientAndPrincipaleTrue(Client client);
    
    List<Adresse> findByClientAndId(Client client, Long id);
}
