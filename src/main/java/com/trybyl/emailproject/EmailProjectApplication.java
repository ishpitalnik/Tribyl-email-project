package com.trybyl.emailproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EmailProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailProjectApplication.class, args);
	}

}
