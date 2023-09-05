package com.ap.homebanking.services;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAllAccount();

    Account getAccountById(long id);

    Account getAccountByNumber(String number);

    void saveAccount(Account account);
}
