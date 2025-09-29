package com.example.livraison.repository;

import com.example.livraison.entity.Commercant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommercantRepository extends JpaRepository<Commercant, String> {
    
    Optional<Commercant> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByIdCommercant(String idCommercant);
}
