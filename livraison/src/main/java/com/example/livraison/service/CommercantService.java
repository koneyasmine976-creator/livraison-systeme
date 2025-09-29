package com.example.livraison.service;

import com.example.livraison.dto.InscriptionCommercantRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.entity.Commercant;
import com.example.livraison.exception.UserAlreadyExistsException;
import com.example.livraison.exception.UserNotFoundException;
import com.example.livraison.repository.CommercantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommercantService {
    
    private final CommercantRepository commerçantRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Commercant inscrireCommercant(InscriptionCommercantRequest request) {
        // Vérifier si l'email existe déjà
        if (commerçantRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Un commerçant avec l'email '" + request.getEmail() + "' existe déjà");
        }
        
        // Vérifier si l'ID commerçant existe déjà
        if (commerçantRepository.existsByIdCommercant(request.getIdCommercant())) {
            throw new UserAlreadyExistsException("Un commerçant avec l'ID '" + request.getIdCommercant() + "' existe déjà");
        }
        
        // Créer le nouveau commerçant
        Commercant commerçant = new Commercant();
        commerçant.setIdCommercant(request.getIdCommercant());
        commerçant.setNom(request.getNom());
        commerçant.setPrenom(request.getPrenom());
        commerçant.setEmail(request.getEmail());
        commerçant.setTelephone(request.getTelephone());
        commerçant.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        commerçant.setNomBoutique(request.getNomBoutique());
        commerçant.setAdresseBoutique(request.getAdresseBoutique());
        commerçant.setLogoBoutique(request.getLogoBoutique());
        
        return commerçantRepository.save(commerçant);
    }
    
    public Optional<Commercant> trouverParEmail(String email) {
        return commerçantRepository.findByEmail(email);
    }
    
    public UserInfo convertirEnUserInfo(Commercant commerçant) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(commerçant.getIdCommercant());
        userInfo.setNom(commerçant.getNom());
        userInfo.setPrenom(commerçant.getPrenom());
        userInfo.setEmail(commerçant.getEmail());
        userInfo.setTelephone(commerçant.getTelephone());
        userInfo.setRole("COMMERCANT");
        userInfo.setDateInscription(commerçant.getDateInscription());
        userInfo.setNomBoutique(commerçant.getNomBoutique());
        userInfo.setAdresseBoutique(commerçant.getAdresseBoutique());
        userInfo.setLogoBoutique(commerçant.getLogoBoutique());
        
        return userInfo;
    }
}
