package com.example.livraison.service;

import com.example.livraison.dto.InscriptionLivreurRequest;
import com.example.livraison.entity.Livreur;
import com.example.livraison.exception.EmailDejaUtiliseException;
import com.example.livraison.exception.TelephoneDejaUtiliseException;
import com.example.livraison.repository.LivreurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LivreurService {
    
    private final LivreurRepository livreurRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Livreur inscrireLivreur(InscriptionLivreurRequest request) {
        // Vérifier si l'email existe déjà
        if (livreurRepository.existsByEmail(request.getEmail())) {
            throw new EmailDejaUtiliseException("Cet email est déjà utilisé par un autre livreur");
        }
        
        // Vérifier si le téléphone existe déjà
        if (livreurRepository.existsByTelephone(request.getTelephone())) {
            throw new TelephoneDejaUtiliseException("Ce numéro de téléphone est déjà utilisé");
        }
        
        Livreur livreur = new Livreur();
        livreur.setIdLivreur(request.getIdLivreur());
        livreur.setNom(request.getNom());
        livreur.setPrenom(request.getPrenom());
        livreur.setTelephone(request.getTelephone());
        livreur.setEmail(request.getEmail());
        livreur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        livreur.setNumeroPermis(request.getNumeroPermis());
        livreur.setTypeVehicule(request.getTypeVehicule());
        livreur.setPlaqueVehicule(request.getPlaqueVehicule());
        livreur.setStatut(Livreur.StatutLivreur.DISPONIBLE);
        livreur.setActif(true);
        
        return livreurRepository.save(livreur);
    }
    
    @Transactional(readOnly = true)
    public Optional<Livreur> trouverParEmail(String email) {
        return livreurRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Livreur> trouverParId(String id) {
        return livreurRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Livreur> obtenirLivreursDisponibles() {
        return livreurRepository.findLivreursDisponibles();
    }
    
    @Transactional(readOnly = true)
    public List<Livreur> obtenirTousLesLivreursActifs() {
        return livreurRepository.findByActifTrue();
    }
    
    public Livreur modifierStatut(String livreurId, Livreur.StatutLivreur nouveauStatut) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        livreur.setStatut(nouveauStatut);
        return livreurRepository.save(livreur);
    }
    
    public Livreur activerDesactiverLivreur(String livreurId, boolean actif) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        livreur.setActif(actif);
        if (!actif) {
            livreur.setStatut(Livreur.StatutLivreur.HORS_SERVICE);
        }
        
        return livreurRepository.save(livreur);
    }
    
    @Transactional(readOnly = true)
    public long compterLivreursActifs() {
        return livreurRepository.countLivreursActifs();
    }
}
