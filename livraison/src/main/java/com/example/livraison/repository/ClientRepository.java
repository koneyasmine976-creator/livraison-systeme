package com.example.livraison.repository;

import com.example.livraison.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    
    Optional<Client> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByIdClient(String idClient);
}
