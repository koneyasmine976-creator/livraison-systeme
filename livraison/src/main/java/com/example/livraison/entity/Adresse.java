package com.example.livraison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rue", nullable = false)
    private String rue;
    
    @Column(name = "ville", nullable = false)
    private String ville;
    
    @Column(name = "code_postal", nullable = false)
    private String codePostal;
    
    @Column(name = "pays", nullable = false)
    private String pays;
    
    @Column(name = "complement")
    private String complement;
    
    @Column(name = "telephone")
    private String telephone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private Client client;
    
    @Column(name = "principale")
    private Boolean principale = false;
}
