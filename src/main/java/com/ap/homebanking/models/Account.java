package com.ap.homebanking.models;

import com.ap.homebanking.dtos.ClientDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;

    private LocalDate date;

    private double balance;

    public Account() {}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="owner_id")
    private Client owner;

    public Account(String number, LocalDate date, double balance) {
        this.number = number;
        this.date = date;
        this.balance = balance;
    }

    public long getId() {return id;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}

    public Double getBalance() {return balance;}

    public void setBalance(Double balance) {this.balance = balance;}

    public Client getOwner() {return owner;}

    public void setOwner(Client owner) {this.owner = owner;}
}
