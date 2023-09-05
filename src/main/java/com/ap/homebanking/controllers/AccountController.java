package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
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

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAllAccount();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getClient(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientService.getClientAccounts(authentication.getName());
    }

    @RequestMapping(path= "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        String userEmail = authentication.getName();
        Client client = clientService.getClientByEmail(authentication.getName());
        if(client != null){
            System.out.println(client.getAccounts().size());
            if(client.getAccounts().size() > 2){
                return new ResponseEntity<>("\n" +
                        "already have 3 accounts", HttpStatus.FORBIDDEN);
            }else{
                String number = "";
                while(true){
                    Random random = new Random(); // Crear una instancia de la clase Random
                    int n = random.nextInt(90000000) + 10000000; // Generar un número aleatorio de 8 dígitos
                    number = "VIN-" + n;
                    Account existsAccount = accountService.getAccountByNumber(number);
                    if(existsAccount == null) break;
                }
                LocalDate today = LocalDate.now();
                Account acc = new Account(number, today, 0);
                client.addAccount(acc);
                clientService.saveClient(client);
                acc.setOwner(client);
                accountService.saveAccount(acc);
                return new ResponseEntity<>("Account created", HttpStatus.CREATED);
            }
        }else{
            throw new UsernameNotFoundException("Unknown user: " + userEmail);
        }
    }
}
