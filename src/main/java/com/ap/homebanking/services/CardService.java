package com.ap.homebanking.services;

import com.ap.homebanking.models.Card;

public interface CardService {

    Card getCardByNumber(String number);

    void saveCard(Card card);
}
