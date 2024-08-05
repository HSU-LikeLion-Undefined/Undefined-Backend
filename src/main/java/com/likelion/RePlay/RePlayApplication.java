package com.likelion.RePlay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class RePlayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RePlayApplication.class, args);
	}

	public static void printName(String name) {
		System.out.println(name);
	}
}
