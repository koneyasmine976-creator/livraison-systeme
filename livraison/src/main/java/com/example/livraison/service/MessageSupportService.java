package com.example.livraison.service;

import com.example.livraison.dto.MessageSupportRequest;
import com.example.livraison.dto.MessageSupportResponse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commercant;
import com.example.livraison.entity.MessageSupport;
import com.example.livraison.repository.MessageSupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageSupportService {
    
    private final MessageSupportRepository messageSupportRepository;
    private final ClientService clientService;
    private final CommercantService commerçantService;
    
    public MessageSupportResponse envoyerMessage(String userEmail, String userRole, MessageSupportRequest request) {
        MessageSupport message = new MessageSupport();
        message.setObjet(request.getObjet());
        message.setContenu(request.getContenu());
        message.setPriorite(request.getPriorite());
        
        if ("CLIENT".equals(userRole)) {
            Client client = clientService.trouverParEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            message.setClient(client);
        } else if ("COMMERCANT".equals(userRole)) {
            Commercant commerçant = commerçantService.trouverParEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
            message.setCommerçant(commerçant);
        }
        
        MessageSupport messageSauvegardé = messageSupportRepository.save(message);
        return convertirEnMessageSupportResponse(messageSauvegardé);
    }
    
    public List<MessageSupportResponse> getMessagesUtilisateur(String userEmail, String userRole) {
        if ("CLIENT".equals(userRole)) {
            Client client = clientService.trouverParEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            List<MessageSupport> messages = messageSupportRepository.findByClientOrderByDateEnvoiDesc(client);
            return messages.stream()
                    .map(this::convertirEnMessageSupportResponse)
                    .collect(Collectors.toList());
        } else if ("COMMERCANT".equals(userRole)) {
            Commercant commerçant = commerçantService.trouverParEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Commerçant non trouvé"));
            List<MessageSupport> messages = messageSupportRepository.findByCommerçantOrderByDateEnvoiDesc(commerçant);
            return messages.stream()
                    .map(this::convertirEnMessageSupportResponse)
                    .collect(Collectors.toList());
        }
        
        throw new RuntimeException("Rôle non autorisé");
    }
    
    public List<MessageSupportResponse> getTousMessages() {
        List<MessageSupport> messages = messageSupportRepository.findAll();
        return messages.stream()
                .map(this::convertirEnMessageSupportResponse)
                .collect(Collectors.toList());
    }
    
    public MessageSupportResponse repondreMessage(Long messageId, String reponse) {
        MessageSupport message = messageSupportRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));
        
        message.setReponse(reponse);
        message.setStatut(MessageSupport.StatutMessage.RESOLU);
        message.setDateReponse(java.time.LocalDateTime.now());
        
        MessageSupport messageModifie = messageSupportRepository.save(message);
        return convertirEnMessageSupportResponse(messageModifie);
    }
    
    private MessageSupportResponse convertirEnMessageSupportResponse(MessageSupport message) {
        MessageSupportResponse response = new MessageSupportResponse();
        response.setId(message.getId());
        response.setObjet(message.getObjet());
        response.setContenu(message.getContenu());
        response.setDateEnvoi(message.getDateEnvoi());
        response.setStatut(message.getStatut().name());
        response.setStatutDescription(message.getStatut().getDescription());
        response.setReponse(message.getReponse());
        response.setDateReponse(message.getDateReponse());
        response.setPriorite(message.getPriorite());
        
        if (message.getClient() != null) {
            response.setExpediteurNom(message.getClient().getPrenom() + " " + message.getClient().getNom());
            response.setExpediteurEmail(message.getClient().getEmail());
            response.setExpediteurRole("CLIENT");
        } else if (message.getCommerçant() != null) {
            response.setExpediteurNom(message.getCommerçant().getPrenom() + " " + message.getCommerçant().getNom());
            response.setExpediteurEmail(message.getCommerçant().getEmail());
            response.setExpediteurRole("COMMERCANT");
        }
        
        return response;
    }
}
