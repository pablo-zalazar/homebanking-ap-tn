package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;

    private String firstName;

    private String lastName;

    private String email;

//    private String password;

    private Set<AccountDTO> accounts = new HashSet<>();

    private Set<ClientLoanDTO> clientLoans = new HashSet<>();

   private Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());
        this.clientLoans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toSet());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(toSet());
//        this.password = client.getPassword();
    }

    public long getId() {return id;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getEmail() {return email;}

//    public String getPassword() {return password;}

    public Set<AccountDTO> getAccounts() {return accounts;}

    public Set<ClientLoanDTO> getLoans() {return clientLoans;}

    public Set<CardDTO> getCards() {return cards;}
}

