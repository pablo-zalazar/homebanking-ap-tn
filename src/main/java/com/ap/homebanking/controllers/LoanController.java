package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.*;
import com.ap.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @RequestMapping("/loans")
    public List<LoanDTO> getAllLoans(){
        return loanService.getAllLoans();
    }

    @Transactional
    @RequestMapping(path= "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> creatLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication){
        long loanId = loanApplication.getLoanId();
        double amount = loanApplication.getAmount();
        Integer payments = loanApplication.getPayments();
        String toAccountNumber = loanApplication.getToAccountNumber();

        if(loanId < 1) {
            return new ResponseEntity<>("LoanId cannot be 0 or negative", HttpStatus.FORBIDDEN);
        }

        if(amount <= 0) {
            return new ResponseEntity<>("Amount cannot be 0 or negative", HttpStatus.FORBIDDEN);
        }

        if(payments <= 0) {
            return new ResponseEntity<>("Payments cannot be 0 or negative", HttpStatus.FORBIDDEN);
        }

        if(toAccountNumber.isEmpty()){
            return new ResponseEntity<>("Missing toAccountNumber", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.getLoanById(loanId);
        if(loan == null){
            return new ResponseEntity<>("Loan dont exists", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount() < amount){
            return new ResponseEntity<>("Amount is greater than loan max amount", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("Amount of payments is invalid", HttpStatus.FORBIDDEN);
        }

        Account account = accountService.getAccountByNumber(toAccountNumber);
        if(account == null){
            return new ResponseEntity<>("Account with number " + toAccountNumber + " don't exists", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();
        HashSet<String> accountsNumber = new HashSet<String>();
        for(Account acc:accounts){
            accountsNumber.add(acc.getNumber());
        }

        if(!accountsNumber.contains(toAccountNumber)){
            return new ResponseEntity<>("The user is not the owner of the account" + toAccountNumber, HttpStatus.FORBIDDEN);
        }

        LocalDateTime today = LocalDateTime.now();
        Transaction transaction = new Transaction(TransactionType.CREDIT, amount * 1.2, loan.getName() + " loan approved", today);
        transactionService.saveTransaction(transaction);

        ClientLoan clientLoan = new ClientLoan(amount, payments);
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);

        clientLoanService.saveClientLoan(clientLoan);
        account.setBalance(account.getBalance() + amount);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }
}
