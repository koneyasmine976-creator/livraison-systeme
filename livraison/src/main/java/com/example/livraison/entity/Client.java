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
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client implements UserDetails {
    
    @Id
    @Column(name = "id_client")
    private String idClient;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "pays_residence")
    private String paysResidence;
    
    @Column(name = "date_inscription")
    private LocalDate dateInscription;
    
    @Column(name = "statut_compte")
    private Boolean statutCompte = true;
    
    @Column(name = "premiere_connexion")
    private Boolean premiereConnexion = true;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Commande> historiqueCommandes;
    
    @PrePersist
    protected void onCreate() {
        if (dateInscription == null) {
            dateInscription = LocalDate.now();
        }
    }
    
    // Implémentation de UserDetails pour Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
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
        return statutCompte;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return statutCompte;
    }
}
