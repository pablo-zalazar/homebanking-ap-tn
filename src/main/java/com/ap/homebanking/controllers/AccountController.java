package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
//        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getClient(@PathVariable Long id){
        AccountDTO account = new AccountDTO(accountRepository.findById(id).orElse(null));
        return account;
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping(path= "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail);
        if(client != null){
            System.out.println(client.getAccounts().size());
            if(client.getAccounts().size() > 2){
                return new ResponseEntity<>("\n" +
                        "already have 3 accounts", HttpStatus.FORBIDDEN);
            }else{
                Random random = new Random(); // Crear una instancia de la clase Random
                int n = random.nextInt(90000000) + 10000000; // Generar un número aleatorio de 8 dígitos
                String number = "VIN-" + n;
                LocalDate today = LocalDate.now();
                Account acc = new Account(number, today, 0);
                client.addAccount(acc);
                clientRepository.save(client);
                acc.setOwner(client);
                accountRepository.save(acc);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }else{
            throw new UsernameNotFoundException("Unknown user: " + userEmail);
        }
    }
}
