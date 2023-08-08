package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {SpringApplication.run(HomebankingApplication.class, args);}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
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
		});
	}
}
