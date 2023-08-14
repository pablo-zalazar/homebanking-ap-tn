package com.ap.homebanking;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {SpringApplication.run(HomebankingApplication.class, args);}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoan){
		return (args -> {
			Client client1 = new Client("Melba", "Morel", "melba_morel@gmail.com");
			Client client2 = new Client("Pablo", "Zalazar", "pablo_zalazar@gmail.com");

			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);
			Account account1 = new Account("VIN001", today, 5000);
			Account account2 = new Account("VIN002", tomorrow, 7500);
			Account account3 = new Account("VIN003", today, 2500);
			Account account4 = new Account("VIN004", tomorrow, 10000);

			clientRepository.save(client1);
			clientRepository.save(client2);

			account1.setOwner(client1);
			account2.setOwner(client1);
			account3.setOwner(client2);
			account4.setOwner(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction tran1 = new Transaction(TransactionType.CREDIT, 15000, "Transaccion 1", LocalDateTime.now());
			Transaction tran2 = new Transaction(TransactionType.DEBIT, -5000, "Transaccion 2", LocalDateTime.now());
			Transaction tran3 = new Transaction(TransactionType.CREDIT, 20000, "Transaccion 3", LocalDateTime.now());

			tran1.setAccount(account1);
			tran2.setAccount(account1);
			tran3.setAccount(account4);

			transactionRepository.save(tran1);
			transactionRepository.save(tran2);
			transactionRepository.save(tran3);

			Loan loanHipotecario = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60));
			Loan loanPersonal = new Loan("Personal", 100000, List.of(6,12,24));
			Loan loanAutomotriz = new Loan("Automotriz", 300000, List.of(6,12,24,36));

			loanRepository.save(loanHipotecario);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanAutomotriz);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			clientLoan1.setClient(client1);
			clientLoan1.setLoan(loanHipotecario);

			clientLoan2.setClient(client1);
			clientLoan2.setLoan(loanPersonal);

			clientLoan3.setClient(client2);
			clientLoan3.setLoan(loanPersonal);

			clientLoan4.setClient(client2);
			clientLoan4.setLoan(loanAutomotriz);

			clientLoan.save(clientLoan1);
			clientLoan.save(clientLoan2);
			clientLoan.save(clientLoan3);
			clientLoan.save(clientLoan4);


		});
	}
}
