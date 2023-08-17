package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;

    private String number;

    private int cvv;

    private LocalDate fromDate;

    private LocalDate thruDate;

    private CardType type;

    private CardColor color;

    private String cardHolder;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.type = card.getType();
        this.color = card.getColor();
        this.cardHolder = card.getCardHolder();
    }

    public long getId() {return id;}

    public String getNumber() {return number;}

    public int getCvv() {return cvv;}

    public LocalDate getFromDate() {return fromDate;}

    public LocalDate getThruDate() {return thruDate;}

    public CardType getType() {return type;}

    public CardColor getColor() {return color;}

    public String getCardHolder() {return cardHolder;}
}
