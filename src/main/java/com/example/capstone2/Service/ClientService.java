package com.example.capstone2.Service;

import com.example.capstone2.Model.Client;
import com.example.capstone2.Repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {


    private final ClientRepository clientRepository ;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public void addClient(Client client ) {
        clientRepository.save(client);
    }

    public Boolean updateClient(Integer clientId, Client client) {
        Client c= clientRepository.findClientByClientId(clientId);
        if (c != null) {
            c.setClientId(client.getClientId());
            c.setUserName(client.getUserName());
            c.setPassword(client.getPassword());
            c.setEmail(client.getEmail());
            c.setRegistrationDate(LocalDateTime.now());
            clientRepository.save(c);
            return true;
        }
        return false;
    }

    public Boolean deleteClient(Integer clientId){
        Client c = clientRepository.findClientByClientId(clientId);
        if (c != null) {
            clientRepository.delete(c);
            return true;
        }
        return false;
    }
}
