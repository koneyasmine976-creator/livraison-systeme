package com.example.livraison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @Column(name = "stock")
    private Integer stock;
    
    @Column(name = "categorie")
    private String categorie;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_commercant")
    private Commercant commerçant;
    
    @Column(name = "actif")
    private Boolean actif = true;
}
