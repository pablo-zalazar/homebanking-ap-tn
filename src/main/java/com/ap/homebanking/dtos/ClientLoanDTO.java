package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ClientLoanDTO {

    private long id;

    private long loanId;

    private double amount;

    private String name;

    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.amount = clientLoan.getAmount();
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPayments();;
    }

    public long getId() {return id;}

    public long getLoanId() {return loanId;}

    public double getAmount() {return amount;}

    public String getName() {return name;}

    public int getPayments() {return payments;}
}
