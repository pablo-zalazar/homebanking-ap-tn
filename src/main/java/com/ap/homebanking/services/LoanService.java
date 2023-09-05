package com.ap.homebanking.services;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getAllLoans();

    Loan getLoanById(long id);
}
