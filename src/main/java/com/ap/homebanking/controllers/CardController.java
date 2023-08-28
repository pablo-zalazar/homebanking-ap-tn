package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.CardDTO;
import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().map(CardDTO::new).collect(toList());
    }

    @RequestMapping(path="/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards();

        if(cards.size() > 5){
            return new ResponseEntity<>("You can't create more cards", HttpStatus.FORBIDDEN);
        }

        byte typeCardsAmount = 0;
        HashSet<CardColor> colorsCards = new HashSet<CardColor>();
        for(Card card:cards){
            if(card.getType() == cardType)
            {
                typeCardsAmount++;
                colorsCards.add(card.getColor());
            }
        }
        if(typeCardsAmount > 2) {
            return new ResponseEntity<>("Already have 3 " + cardType + " cards", HttpStatus.FORBIDDEN);
        }
        if(colorsCards.contains(cardColor)) {
            return new ResponseEntity<>("Already have a " + cardType.toString().toLowerCase() + " " + cardColor.toString().toLowerCase() + " card", HttpStatus.FORBIDDEN);
        }

        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        String n = "";
        String number = "";
        for(int i = 0; i<4; i++){
            n = Integer.toString(random.nextInt(900) + 100);
            if(i!=3) number = number + n + "-";
            else number = number + n;
        }

        String cardHolder = client.getFirstName() + " " + client.getLastName();
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);
        Card card = new Card(number,cvv,fromDate,thruDate,cardHolder,cardType,cardColor);
        card.setOwner(client);
        cardRepository.save(card);
        return new ResponseEntity<>("Card created",HttpStatus.CREATED);
    }
}
