package com.example.livraison.service;

import com.example.livraison.dto.ConnexionRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commercant;
import com.example.livraison.exception.AccountBlockedException;
import com.example.livraison.exception.InvalidCredentialsException;
import com.example.livraison.exception.InvalidRoleException;
import com.example.livraison.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final ClientService clientService;
    private final CommercantService commerçantService;
    private final PasswordEncoder passwordEncoder;
    
    public UserInfo connecter(ConnexionRequest request) {
        String email = request.getEmail();
        String motDePasse = request.getMotDePasse();
        String role = request.getRole().toUpperCase();
        
        if ("CLIENT".equals(role)) {
            return connecterClient(email, motDePasse);
        } else if ("COMMERCANT".equals(role)) {
            return connecterCommercant(email, motDePasse);
        } else {
            throw new InvalidRoleException("Rôle invalide '" + role + "'. Utilisez 'CLIENT' ou 'COMMERCANT'");
        }
    }
    
    private UserInfo connecterClient(String email, String motDePasse) {
        Optional<Client> clientOpt = clientService.trouverParEmail(email);
        if (clientOpt.isEmpty()) {
            throw new UserNotFoundException("Aucun client trouvé avec l'email '" + email + "'");
        }
        
        Client client = clientOpt.get();
        if (!passwordEncoder.matches(motDePasse, client.getMotDePasse())) {
            throw new InvalidCredentialsException("Mot de passe incorrect pour l'email '" + email + "'");
        }
        
        if (!client.getStatutCompte()) {
            throw new AccountBlockedException("Le compte client avec l'email '" + email + "' est bloqué");
        }
        
        // Marquer la première connexion comme terminée si c'est le cas
        if (client.getPremiereConnexion()) {
            clientService.marquerPremiereConnexionTerminee(email);
        }
        
        return clientService.convertirEnUserInfo(client);
    }
    
    private UserInfo connecterCommercant(String email, String motDePasse) {
        Optional<Commercant> commerçantOpt = commerçantService.trouverParEmail(email);
        if (commerçantOpt.isEmpty()) {
            throw new UserNotFoundException("Aucun commerçant trouvé avec l'email '" + email + "'");
        }
        
        Commercant commerçant = commerçantOpt.get();
        if (!passwordEncoder.matches(motDePasse, commerçant.getMotDePasse())) {
            throw new InvalidCredentialsException("Mot de passe incorrect pour l'email '" + email + "'");
        }
        
        return commerçantService.convertirEnUserInfo(commerçant);
    }
}
