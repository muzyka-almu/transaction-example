package com.example.transactionexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("com.example.transactionexample")
@EnableTransactionManagement
public class TransactionExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionExampleApplication.class, args);
	}
}
