package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;

import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClients();
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.getClientByID(id));
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return new ClientDTO(clientService.getClientByEmail(authentication.getName()));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing firstName", HttpStatus.FORBIDDEN);
        }

        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing lastName", HttpStatus.FORBIDDEN);
        }

        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }
        if (!matcher.matches()) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.FORBIDDEN);
        }

        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }
        if (password.length() < 6) {
            return new ResponseEntity<>("Invalid password length", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));

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
        return new ResponseEntity<>("Client created",HttpStatus.CREATED);
    }
}
