package com.example.livraison.service;

import com.example.livraison.dto.InscriptionClientRequest;
import com.example.livraison.dto.UserInfo;
import com.example.livraison.entity.Client;
import com.example.livraison.exception.UserAlreadyExistsException;
import com.example.livraison.exception.UserNotFoundException;
import com.example.livraison.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Client inscrireClient(InscriptionClientRequest request) {
        // Vérifier si l'email existe déjà
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Un client avec l'email '" + request.getEmail() + "' existe déjà");
        }
        
        // Vérifier si l'ID client existe déjà
        if (clientRepository.existsByIdClient(request.getIdClient())) {
            throw new UserAlreadyExistsException("Un client avec l'ID '" + request.getIdClient() + "' existe déjà");
        }
        
        // Créer le nouveau client
        Client client = new Client();
        client.setIdClient(request.getIdClient());
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        client.setAdresse(request.getAdresse());
        client.setPaysResidence(request.getPaysResidence());
        client.setStatutCompte(true);
        client.setPremiereConnexion(true);
        
        return clientRepository.save(client);
    }
    
    public Optional<Client> trouverParEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    
    public UserInfo convertirEnUserInfo(Client client) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(client.getIdClient());
        userInfo.setNom(client.getNom());
        userInfo.setPrenom(client.getPrenom());
        userInfo.setEmail(client.getEmail());
        userInfo.setTelephone(client.getTelephone());
        userInfo.setRole("CLIENT");
        userInfo.setDateInscription(client.getDateInscription());
        userInfo.setAdresse(client.getAdresse());
        userInfo.setPaysResidence(client.getPaysResidence());
        userInfo.setStatutCompte(client.getStatutCompte());
        userInfo.setPremiereConnexion(client.getPremiereConnexion());
        
        return userInfo;
    }
    
    public void marquerPremiereConnexionTerminee(String email) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setPremiereConnexion(false);
            clientRepository.save(client);
        }
    }
}
