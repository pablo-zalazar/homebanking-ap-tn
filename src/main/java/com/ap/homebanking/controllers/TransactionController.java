package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import com.ap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @RequestMapping(path="/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, Authentication authentication){

        if(amount <= 0) {
            return new ResponseEntity<>("Missing Amount", HttpStatus.FORBIDDEN);
        }

        if (description.isEmpty()) {
            return new ResponseEntity<>("Missing Description", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing FromAccountNumber", HttpStatus.FORBIDDEN);
        }

        if (toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing ToAccountNumber", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("The account numbers are the same", HttpStatus.FORBIDDEN);
        }

        Account fromAccount = accountService.getAccountByNumber(fromAccountNumber);
        if(fromAccount == null){
            return new ResponseEntity<>("Account from number " + fromAccountNumber + " don't exists", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();
        HashSet<String> accountsNumber = new HashSet<String>();
        for(Account acc:accounts){
            accountsNumber.add(acc.getNumber());
        }

        if(!accountsNumber.contains(fromAccountNumber)){
            return new ResponseEntity<>("The user is not the owner of the account" + fromAccountNumber, HttpStatus.FORBIDDEN);
        }

        Account toAccount = accountService.getAccountByNumber(toAccountNumber);
        if(toAccount == null){
            return new ResponseEntity<>("Account to number " + toAccountNumber + " don't exists", HttpStatus.FORBIDDEN);
        }

        if(fromAccount.getBalance() < amount){
            return new ResponseEntity<>("the account " + fromAccountNumber + " does not have enough balance", HttpStatus.FORBIDDEN);
        }

        LocalDateTime today = LocalDateTime.now();
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount, description + " " + fromAccountNumber, today);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description + " " + toAccountNumber, today);

        debitTransaction.setAccount(fromAccount);
        creditTransaction.setAccount(toAccount);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountService.saveAccount(fromAccount);
        accountService.saveAccount(toAccount);

        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }
}
