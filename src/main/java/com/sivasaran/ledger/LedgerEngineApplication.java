package com.sivasaran.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LedgerEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedgerEngineApplication.class, args);
	}

}
