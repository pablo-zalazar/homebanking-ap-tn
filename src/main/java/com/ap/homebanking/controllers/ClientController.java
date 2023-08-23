package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
//        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        ClientDTO client = new ClientDTO(clientRepository.findById(id).orElse(null));
        return client;
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        ClientDTO client = new ClientDTO(clientRepository.findByEmail(authentication.getName()));
        return client;
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);

        //System.out.println(email);
        //System.out.println(matcher.matches());
        //System.out.println(matcher.find());

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

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
