package com.ap.homebanking.services;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.CardDTO;
import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getAllClients();

    Client getClientByID(long id);

    Client getClientByEmail(String email);

    List<AccountDTO> getClientAccounts(String email);

    List<CardDTO> getClientCards(String email);

    void saveClient(Client client);
}
