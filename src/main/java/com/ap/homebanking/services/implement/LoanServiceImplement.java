package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Loan;
import com.ap.homebanking.repositories.LoanRepository;
import com.ap.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Override
    public Loan getLoanById(long id) {
        Optional<Loan> loan = loanRepository.findById(id);
        return loan.orElse(null);
    }
}
