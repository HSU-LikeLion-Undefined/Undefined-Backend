package com.likelion.RePlay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RePlayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RePlayApplication.class, args);
	}

	public static void printName(String name) {
		System.out.println(name);
	}
}
