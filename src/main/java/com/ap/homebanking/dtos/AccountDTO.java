package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;

import java.time.LocalDate;

public class AccountDTO {
    private long id;

    private String number;

    private LocalDate date;

    private double balance;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
    }

    public long getId() {return id;}

    public String getNumber() {return number;}

    public LocalDate getDate() {return date;}

    public double getBalance() {return balance;}
}
