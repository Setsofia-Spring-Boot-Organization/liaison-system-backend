package com.backend.liaison_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class LiaisonSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiaisonSystemApplication.class, args);
	}

}
