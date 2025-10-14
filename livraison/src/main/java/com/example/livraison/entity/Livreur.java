package com.example.livraison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "livreurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livreur implements UserDetails {
    
    @Id
    @Column(name = "id_livreur")
    private String idLivreur;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Column(name = "telephone", nullable = false)
    private String telephone;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    
    @Column(name = "numero_permis")
    private String numeroPermis;
    
    @Column(name = "type_vehicule")
    private String typeVehicule;
    
    @Column(name = "plaque_vehicule")
    private String plaqueVehicule;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutLivreur statut = StatutLivreur.DISPONIBLE;
    
    @Column(name = "date_inscription")
    private LocalDate dateInscription;
    
    @Column(name = "actif")
    private Boolean actif = true;
    
    @PrePersist
    protected void onCreate() {
        if (dateInscription == null) {
            dateInscription = LocalDate.now();
        }
    }
    
    // Implémentation de UserDetails pour Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_LIVREUR"));
    }
    
    @Override
    public String getPassword() {
        return motDePasse;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return actif;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return actif;
    }
    
    public enum StatutLivreur {
        DISPONIBLE("Disponible"),
        OCCUPE("Occupé"),
        HORS_SERVICE("Hors service");
        
        private final String description;
        
        StatutLivreur(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
