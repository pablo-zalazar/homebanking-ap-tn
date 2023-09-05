package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.CardDTO;
import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public Client getClientByID(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public List<AccountDTO> getClientAccounts(String email) {
        return clientRepository.findByEmail(email).getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public List<CardDTO> getClientCards(String email) {
        return clientRepository.findByEmail(email).getCards().stream().map(CardDTO::new).collect(toList());
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
