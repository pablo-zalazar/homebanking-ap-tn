package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private long id;

    private String number;

    private LocalDate date;

    private double balance;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
    }

    public long getId() {return id;}

    public String getNumber() {return number;}

    public LocalDate getDate() {return date;}

    public double getBalance() {return balance;}

    public Set<TransactionDTO> getTransactions() {return transactions;}
}
